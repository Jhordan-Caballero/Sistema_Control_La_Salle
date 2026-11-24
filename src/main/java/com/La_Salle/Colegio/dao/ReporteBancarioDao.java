package com.La_Salle.Colegio.dao;

import com.La_Salle.Colegio.model.ReporteBancario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReporteBancarioDao extends JpaRepository<ReporteBancario, Long> {
    boolean existsByNombreArchivo(String nombreArchivo);
}