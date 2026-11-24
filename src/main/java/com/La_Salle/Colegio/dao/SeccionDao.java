package com.La_Salle.Colegio.dao;

import com.La_Salle.Colegio.model.Apoderado;
import com.La_Salle.Colegio.model.Seccion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeccionDao extends JpaRepository<Seccion, Long> {}
