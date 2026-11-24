package com.La_Salle.Colegio.service;

import com.La_Salle.Colegio.dao.CuotaDao;
import com.La_Salle.Colegio.dao.EstudianteDao;
import com.La_Salle.Colegio.dao.PagoDao;
import com.La_Salle.Colegio.model.Cuota;
import com.La_Salle.Colegio.model.Estudiante;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EstadoCuentaService {

    private final EstudianteDao estudianteDao;
    private final CuotaDao cuotaDao;
    private final PagoDao pagoDao;

    // DTOs de respuesta: así no se serializan las entidades completas
    public record EstadoCuenta(String estudiante, BigDecimal deudaTotal, List<CuotaItem> cuotas, List<PagoItem> pagos) {}
    public record CuotaItem(Long id, String concepto, int nro, BigDecimal monto, BigDecimal saldo,
                            LocalDate vencimiento, String estado, boolean vencida) {}
    public record PagoItem(LocalDateTime fecha, BigDecimal monto, String medio, String nroOperacion, boolean extemporaneo) {}

    public EstadoCuenta obtener(Long estudianteId) {
        Estudiante e = estudianteDao.findById(estudianteId)
                .orElseThrow(() -> new IllegalArgumentException("El estudiante no existe"));
        LocalDate hoy = LocalDate.now();

        List<Cuota> cuotas = cuotaDao.findByMatriculaEstudianteIdOrderByFechaVencimientoAsc(estudianteId);
        BigDecimal deuda = cuotas.stream().map(Cuota::getSaldo).reduce(BigDecimal.ZERO, BigDecimal::add);

        List<CuotaItem> items = cuotas.stream().map(c -> new CuotaItem(
                c.getId(), c.getConcepto(), c.getNroCuota(), c.getMonto(), c.getSaldo(), c.getFechaVencimiento(),
                c.getEstado(), c.getSaldo().signum() > 0 && c.getFechaVencimiento().isBefore(hoy))).toList();

        List<PagoItem> pagos = pagoDao.findByCuotaMatriculaEstudianteIdOrderByFechaPagoDesc(estudianteId).stream()
                .map(p -> new PagoItem(p.getFechaPago(), p.getMonto(), p.getMedioPago(),
                        p.getNroOperacion(), p.isExtemporaneo())).toList();

        return new EstadoCuenta(e.getNombres() + " " + e.getApellidos(), deuda, items, pagos);
    }
}