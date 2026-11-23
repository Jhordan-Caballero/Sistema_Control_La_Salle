package com.La_Salle.Colegio.dao;

import com.La_Salle.Colegio.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;


@Repository
public interface EstudianteDao extends JpaRepository<Estudiante, Long> {
}
