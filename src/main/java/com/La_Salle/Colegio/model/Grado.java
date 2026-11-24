package com.La_Salle.Colegio.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "grado")
@Getter @Setter
public class Grado {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_grado")
    private Long id;

    @Column(nullable = false, length = 20) private String nivel;   // PRIMARIA, SECUNDARIA
    @Column(nullable = false, length = 50) private String nombre;
}
