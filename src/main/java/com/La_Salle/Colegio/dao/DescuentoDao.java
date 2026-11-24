package com.La_Salle.Colegio.dao;

import com.La_Salle.Colegio.model.Descuento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DescuentoDao extends JpaRepository<Descuento, Long> {
    boolean existsByMatriculaIdAndTipo(Long matriculaId, String tipo);
}