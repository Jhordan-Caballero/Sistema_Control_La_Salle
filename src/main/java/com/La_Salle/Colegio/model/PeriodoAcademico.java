package com.La_Salle.Colegio.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "periodo_academico")
@Getter @Setter
public class PeriodoAcademico {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_periodo")
    private Long id;

    @Column(nullable = false, unique = true) private int anio;
    @Column(name = "inicio_matricula", nullable = false) private LocalDate inicioMatricula;
    @Column(name = "fin_matricula", nullable = false) private LocalDate finMatricula;
    @Column(nullable = false, length = 20) private String estado;  // ACTIVO, CERRADO
}