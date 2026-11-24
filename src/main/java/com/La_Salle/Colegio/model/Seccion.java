package com.La_Salle.Colegio.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "seccion")
@Getter @Setter
public class Seccion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_seccion")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_grado")
    private Grado grado;

    @Column(nullable = false, length = 10) private String nombre;
    @Column(nullable = false) private int vacantes;
}