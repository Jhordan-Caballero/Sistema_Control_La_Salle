package com.La_Salle.Colegio.dao;

import com.La_Salle.Colegio.model.ConvenioFraccionamiento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConvenioFraccionamientoDao extends JpaRepository<ConvenioFraccionamiento, Long> {
    boolean existsByMatriculaEstudianteIdAndEstado(Long estudianteId, String estado);
}