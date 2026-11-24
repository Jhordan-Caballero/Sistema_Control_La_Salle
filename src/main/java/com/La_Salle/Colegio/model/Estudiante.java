package com.La_Salle.Colegio.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "estudiante")
@Getter @Setter
public class Estudiante {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estudiante")
    private Long id;

    @Column(nullable = false, unique = true, length = 20) private String codigo;
    @Column(nullable = false, unique = true, length = 8) private String dni;
    @Column(nullable = false, length = 100) private String nombres;
    @Column(nullable = false, length = 100) private String apellidos;
    @Column(name = "fecha_nacimiento", nullable = false) private LocalDate fechaNacimiento;

    @ManyToOne
    @JoinColumn(name = "id_apoderado")
    private Apoderado apoderado;
}