package com.La_Salle.Colegio.service;

import com.La_Salle.Colegio.dao.*;
import com.La_Salle.Colegio.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BeneficioService {

    private static final BigDecimal CIEN = new BigDecimal("100");

    private final MatriculaDao matriculaDao;
    private final DescuentoDao descuentoDao;
    private final CuotaDao cuotaDao;
    private final ConvenioFraccionamientoDao convenioDao;

    // RF10: descuento por más de dos hermanos matriculados en el mismo periodo
    @Transactional
    public Descuento descuentoHermanos(Long matriculaId) {
        Matricula m = buscarMatricula(matriculaId);
        Apoderado ap = m.getEstudiante().getApoderado();
        if (ap == null)
            throw new IllegalArgumentException("El estudiante no tiene apoderado registrado");

        long hermanos = matriculaDao.countByEstudianteApoderadoIdAndPeriodoId(ap.getId(), m.getPeriodo().getId());
        if (hermanos <= 2)
            throw new IllegalArgumentException("Se requieren más de dos hermanos matriculados (hay " + hermanos + ")");

        return aplicarDescuento(m, "HERMANOS", new BigDecimal("10"), "Más de dos hermanos matriculados");
    }

    // RF11: beca con porcentaje indicado
    @Transactional
    public Descuento beca(Long matriculaId, BigDecimal porcentaje, String motivo) {
        if (porcentaje == null || porcentaje.signum() <= 0 || porcentaje.compareTo(CIEN) > 0)
            throw new IllegalArgumentException("El porcentaje debe estar entre 0 y 100");
        return aplicarDescuento(buscarMatricula(matriculaId), "BECA", porcentaje, motivo);
    }

    private Descuento aplicarDescuento(Matricula m, String tipo, BigDecimal pct, String motivo) {
        if (descuentoDao.existsByMatriculaIdAndTipo(m.getId(), tipo))
            throw new IllegalArgumentException("Ya tiene un beneficio de tipo " + tipo);

        Descuento d = new Descuento();
        d.setMatricula(m);
        d.setTipo(tipo);
        d.setPorcentaje(pct);
        d.setMotivo(motivo);
        d.setEstado("APROBADO");
        descuentoDao.save(d);

        // Recalcula las pensiones pendientes
        BigDecimal factor = BigDecimal.ONE.subtract(pct.divide(CIEN, 4, RoundingMode.HALF_UP));
        for (Cuota c : cuotaDao.findByMatriculaIdAndConceptoAndSaldoGreaterThan(m.getId(), "PENSION", BigDecimal.ZERO)) {
            c.setSaldo(c.getSaldo().multiply(factor).setScale(2, RoundingMode.HALF_UP));
            cuotaDao.save(c);
        }
        return d;
    }

    // RF12: convierte la deuda de una matrícula en N cuotas nuevas
    @Transactional
    public ConvenioFraccionamiento fraccionar(Long matriculaId, int nroCuotas) {
        if (nroCuotas < 2 || nroCuotas > 12)
            throw new IllegalArgumentException("El fraccionamiento debe tener entre 2 y 12 cuotas");

        Matricula m = buscarMatricula(matriculaId);
        List<Cuota> deudas = cuotaDao.findByMatriculaIdAndSaldoGreaterThan(matriculaId, BigDecimal.ZERO);
        if (deudas.isEmpty())
            throw new IllegalArgumentException("No hay deuda para fraccionar");

        BigDecimal total = deudas.stream().map(Cuota::getSaldo).reduce(BigDecimal.ZERO, BigDecimal::add);

        ConvenioFraccionamiento conv = new ConvenioFraccionamiento();
        conv.setMatricula(m);
        conv.setDeudaTotal(total);
        conv.setNroCuotas(nroCuotas);
        conv.setFechaAcuerdo(LocalDate.now());
        conv.setEstado("APROBADO");
        convenioDao.save(conv);

        // Las cuotas antiguas quedan refinanciadas
        deudas.forEach(c -> { c.setSaldo(BigDecimal.ZERO); c.setEstado("REFINANCIADA"); });
        cuotaDao.saveAll(deudas);

        // Crea las nuevas cuotas; la última absorbe el redondeo
        BigDecimal base = total.divide(BigDecimal.valueOf(nroCuotas), 2, RoundingMode.HALF_UP);
        BigDecimal acumulado = BigDecimal.ZERO;
        for (int i = 1; i <= nroCuotas; i++) {
            BigDecimal monto = (i == nroCuotas) ? total.subtract(acumulado) : base;
            acumulado = acumulado.add(monto);

            Cuota c = new Cuota();
            c.setMatricula(m);
            c.setConcepto("FRACCIONAMIENTO");
            c.setNroCuota(i);
            c.setMonto(monto);
            c.setSaldo(monto);
            c.setFechaVencimiento(LocalDate.now().plusMonths(i).withDayOfMonth(5));
            c.setEstado("PENDIENTE");
            cuotaDao.save(c);
        }
        return conv;
    }

    private Matricula buscarMatricula(Long id) {
        return matriculaDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La matrícula no existe"));
    }
}