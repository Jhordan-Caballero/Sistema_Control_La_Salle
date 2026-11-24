package com.La_Salle.Colegio.service;

import com.La_Salle.Colegio.dao.*;
import com.La_Salle.Colegio.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatriculaService {

    private static final BigDecimal MONTO_MATRICULA = new BigDecimal("300.00");
    private static final BigDecimal MONTO_PENSION = new BigDecimal("250.00");

    private final EstudianteDao estudianteDao;
    private final PeriodoAcademicoDao periodoDao;
    private final SeccionDao seccionDao;
    private final MatriculaDao matriculaDao;
    private final CuotaDao cuotaDao;
    private final ConvenioFraccionamientoDao convenioDao;

    @Transactional
    public Matricula matricular(Long estudianteId, Long periodoId, Long seccionId) {
        Estudiante estudiante = estudianteDao.findById(estudianteId)
                .orElseThrow(() -> new IllegalArgumentException("El estudiante no existe"));
        PeriodoAcademico periodo = periodoDao.findById(periodoId)
                .orElseThrow(() -> new IllegalArgumentException("El periodo no existe"));
        Seccion seccion = seccionDao.findById(seccionId)
                .orElseThrow(() -> new IllegalArgumentException("La sección no existe"));

        // El periodo debe estar abierto
        LocalDate hoy = LocalDate.now();
        if (!"ACTIVO".equals(periodo.getEstado()))
            throw new IllegalArgumentException("El periodo no está activo");
        if (hoy.isBefore(periodo.getInicioMatricula()) || hoy.isAfter(periodo.getFinMatricula()))
            throw new IllegalArgumentException("Fuera del periodo de matrícula");
        if (matriculaDao.existsByEstudianteIdAndPeriodoId(estudianteId, periodoId))
            throw new IllegalArgumentException("El estudiante ya está matriculado en este periodo");

        // RF03: deuda del año anterior; solo continúa si hay fraccionamiento aprobado
        BigDecimal deuda = cuotaDao.deudaAnterior(estudianteId, periodo.getAnio());
        boolean convenioAprobado = convenioDao.existsByMatriculaEstudianteIdAndEstado(estudianteId, "APROBADO");
        if (deuda.signum() > 0 && !convenioAprobado)
            throw new IllegalArgumentException("Deuda pendiente del año anterior: S/ " + deuda);

        // RF04: vacantes por sección
        if (matriculaDao.countBySeccionIdAndPeriodoId(seccionId, periodoId) >= seccion.getVacantes())
            throw new IllegalArgumentException("No hay vacantes en esta sección");

        Matricula m = new Matricula();
        m.setEstudiante(estudiante);
        m.setPeriodo(periodo);
        m.setSeccion(seccion);
        m.setFechaMatricula(hoy);
        m.setEstado("MATRICULADO");
        matriculaDao.save(m);

        // RF05: cuotas automáticas (1 de matrícula + 10 pensiones de marzo a diciembre)
        cuotaDao.save(nuevaCuota(m, "MATRICULA", 1, MONTO_MATRICULA, LocalDate.of(periodo.getAnio(), 2, 28)));
        for (int i = 1; i <= 10; i++) {
            cuotaDao.save(nuevaCuota(m, "PENSION", i, MONTO_PENSION, LocalDate.of(periodo.getAnio(), 2 + i, 5)));
        }
        return m;
    }

    private Cuota nuevaCuota(Matricula m, String concepto, int nro, BigDecimal monto, LocalDate vencimiento) {
        Cuota c = new Cuota();
        c.setMatricula(m);
        c.setConcepto(concepto);
        c.setNroCuota(nro);
        c.setMonto(monto);
        c.setSaldo(monto);
        c.setFechaVencimiento(vencimiento);
        c.setEstado("PENDIENTE");
        return c;
    }
    public List<Matricula> listar() {
        return matriculaDao.findAll();
    }
}