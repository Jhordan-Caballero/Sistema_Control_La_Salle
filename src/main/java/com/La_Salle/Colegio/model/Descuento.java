package com.La_Salle.Colegio.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "descuento")
@Getter @Setter
public class Descuento {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_descuento")
    private Long id;

    @ManyToOne(optional = false) @JoinColumn(name = "id_matricula") private Matricula matricula;

    @Column(nullable = false, length = 20) private String tipo;          // HERMANOS, BECA
    @Column(nullable = false, precision = 5, scale = 2) private BigDecimal porcentaje;
    @Column(length = 255) private String motivo;
    @Column(nullable = false, length = 20) private String estado;
}