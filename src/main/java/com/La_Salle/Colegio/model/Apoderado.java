package com.La_Salle.Colegio.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "apoderado")
@Getter @Setter
public class Apoderado {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_apoderado")
    private Long id;

    @Column(nullable = false, unique = true, length = 8) private String dni;
    @Column(nullable = false, length = 100) private String nombres;
    @Column(nullable = false, length = 100) private String apellidos;
    @Column(length = 15) private String telefono;
    @Column(length = 100) private String correo;
}