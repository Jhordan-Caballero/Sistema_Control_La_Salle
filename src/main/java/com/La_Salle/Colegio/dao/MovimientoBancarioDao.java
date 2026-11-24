package com.La_Salle.Colegio.dao;

import com.La_Salle.Colegio.model.MovimientoBancario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimientoBancarioDao extends JpaRepository<MovimientoBancario, Long> {
    List<MovimientoBancario> findByReporteIdAndEstadoConciliacion(Long reporteId, String estado);
}