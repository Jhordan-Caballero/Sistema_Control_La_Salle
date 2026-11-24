package com.La_Salle.Colegio.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "matricula", uniqueConstraints = @UniqueConstraint(columnNames = {"id_estudiante", "id_periodo"}))
@Getter @Setter
public class Matricula {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_matricula")
    private Long id;

    @ManyToOne(optional = false) @JoinColumn(name = "id_estudiante") private Estudiante estudiante;
    @ManyToOne(optional = false) @JoinColumn(name = "id_periodo") private PeriodoAcademico periodo;
    @ManyToOne(optional = false) @JoinColumn(name = "id_seccion") private Seccion seccion;

    @Column(name = "fecha_matricula", nullable = false) private LocalDate fechaMatricula;
    @Column(nullable = false, length = 20) private String estado;
    @Column(length = 255) private String observacion;
}
