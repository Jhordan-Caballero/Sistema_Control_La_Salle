package com.La_Salle.Colegio.dao;

import com.La_Salle.Colegio.model.Cuota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface CuotaDao extends JpaRepository<Cuota, Long> {

    List<Cuota> findByMatriculaEstudianteIdAndSaldoGreaterThanOrderByFechaVencimientoAsc(Long estudianteId, BigDecimal saldo);
    List<Cuota> findByMatriculaEstudianteIdOrderByFechaVencimientoAsc(Long estudianteId);
    List<Cuota> findByMatriculaIdAndConceptoAndSaldoGreaterThan(Long matriculaId, String concepto, BigDecimal saldo);
    List<Cuota> findByMatriculaIdAndSaldoGreaterThan(Long matriculaId, BigDecimal saldo);

    // Deuda de años anteriores al periodo que se quiere matricular
    @Query("select coalesce(sum(c.saldo), 0) from Cuota c " +
            "where c.matricula.estudiante.id = :estudianteId and c.matricula.periodo.anio < :anio")
    BigDecimal deudaAnterior(@Param("estudianteId") Long estudianteId, @Param("anio") int anio);

    @Query("select coalesce(sum(c.saldo), 0) from Cuota c")
    BigDecimal sumSaldo();
}
