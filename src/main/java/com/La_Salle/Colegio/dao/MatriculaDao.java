package com.La_Salle.Colegio.dao;

import com.La_Salle.Colegio.model.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatriculaDao extends JpaRepository<Matricula, Long> {
    boolean existsByEstudianteIdAndPeriodoId(Long estudianteId, Long periodoId);
    long countBySeccionIdAndPeriodoId(Long seccionId, Long periodoId);
    long countByEstudianteApoderadoIdAndPeriodoId(Long apoderadoId, Long periodoId);
}