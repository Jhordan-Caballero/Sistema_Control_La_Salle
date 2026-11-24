package com.La_Salle.Colegio.service;

import com.La_Salle.Colegio.dao.*;
import com.La_Salle.Colegio.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConciliacionService {

    private final ReporteBancarioDao reporteDao;
    private final MovimientoBancarioDao movimientoDao;
    private final EstudianteDao estudianteDao;
    private final CuotaDao cuotaDao;
    private final PagoDao pagoDao;

    /**
     * RF08: importa un CSV con cabecera y columnas:
     * codigoEstudiante;nroOperacion;fecha(yyyy-MM-dd);monto
     */
    @Transactional
    public ReporteBancario importarCsv(String banco, MultipartFile archivo, String usuario) throws IOException {
        String nombre = archivo.getOriginalFilename();
        if (reporteDao.existsByNombreArchivo(nombre))
            throw new IllegalArgumentException("Este reporte ya fue cargado");

        ReporteBancario r = new ReporteBancario();
        r.setBanco(banco);
        r.setNombreArchivo(nombre);
        r.setFechaCarga(LocalDateTime.now());
        r.setUsuario(usuario);
        reporteDao.save(r);

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(archivo.getInputStream(), StandardCharsets.UTF_8))) {
            br.readLine(); // cabecera
            String linea;
            int n = 1;
            while ((linea = br.readLine()) != null) {
                n++;
                if (linea.isBlank()) continue;
                try {
                    String[] c = linea.split(";");
                    MovimientoBancario m = new MovimientoBancario();
                    m.setReporte(r);
                    m.setCodigoEstudiante(c[0].trim());
                    m.setNroOperacion(c[1].trim());
                    m.setFechaMovimiento(LocalDate.parse(c[2].trim()));
                    m.setMonto(new BigDecimal(c[3].trim()));
                    m.setEstadoConciliacion("PENDIENTE");
                    movimientoDao.save(m);
                } catch (RuntimeException e) {
                    throw new IllegalArgumentException("Línea " + n + " inválida: " + linea);
                }
            }
        }
        return r;
    }

    /**
     * RF09: cruza cada movimiento con la deuda del estudiante.
     * Aplica el monto a las cuotas más antiguas y marca como DIFERENCIA lo que no cuadra.
     */
    @Transactional
    public Map<String, Object> conciliar(Long reporteId) {
        List<MovimientoBancario> pendientes =
                movimientoDao.findByReporteIdAndEstadoConciliacion(reporteId, "PENDIENTE");
        int conciliados = 0, diferencias = 0;

        for (MovimientoBancario m : pendientes) {
            Optional<Estudiante> est = estudianteDao.findByCodigo(m.getCodigoEstudiante());
            List<Cuota> cuotas = est.map(e -> cuotaDao
                            .findByMatriculaEstudianteIdAndSaldoGreaterThanOrderByFechaVencimientoAsc(e.getId(), BigDecimal.ZERO))
                    .orElse(List.of());
            BigDecimal deuda = cuotas.stream().map(Cuota::getSaldo).reduce(BigDecimal.ZERO, BigDecimal::add);

            String motivo = null;
            if (est.isEmpty()) motivo = "Código de estudiante no existe";
            else if (pagoDao.existsByNroOperacion(m.getNroOperacion())) motivo = "Operación ya conciliada";
            else if (cuotas.isEmpty()) motivo = "El estudiante no tiene deuda pendiente";
            else if (m.getMonto().compareTo(deuda) > 0) motivo = "El monto supera la deuda pendiente";

            if (motivo != null) {
                m.setEstadoConciliacion("DIFERENCIA");
                m.setObservacion(motivo);
                movimientoDao.save(m);
                diferencias++;
                continue;
            }

            BigDecimal restante = m.getMonto();
            for (Cuota c : cuotas) {
                if (restante.signum() == 0) break;
                BigDecimal aplicar = restante.min(c.getSaldo());
                c.setSaldo(c.getSaldo().subtract(aplicar));
                c.setEstado(c.getSaldo().signum() == 0 ? "PAGADA" : "PARCIAL");
                cuotaDao.save(c);

                Pago p = new Pago();
                p.setCuota(c);
                p.setMovimiento(m);
                p.setFechaPago(LocalDateTime.now());
                p.setMonto(aplicar);
                p.setMedioPago("BANCO");
                p.setNroOperacion(m.getNroOperacion());
                p.setExtemporaneo(c.getFechaVencimiento().isBefore(LocalDate.now()));
                p.setUsuario("CONCILIACION");
                pagoDao.save(p);

                restante = restante.subtract(aplicar);
            }
            m.setEstadoConciliacion("CONCILIADO");
            movimientoDao.save(m);
            conciliados++;
        }
        return Map.of("conciliados", conciliados, "diferencias", diferencias);
    }
}