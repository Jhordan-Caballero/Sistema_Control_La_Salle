package com.La_Salle.Colegio.dao;

import com.La_Salle.Colegio.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface PagoDao extends JpaRepository<Pago, Long> {
    boolean existsByNroOperacion(String nroOperacion);
    List<Pago> findByCuotaMatriculaEstudianteIdOrderByFechaPagoDesc(Long estudianteId);

    @Query("select coalesce(sum(p.monto), 0) from Pago p")
    BigDecimal sumMontos();
}