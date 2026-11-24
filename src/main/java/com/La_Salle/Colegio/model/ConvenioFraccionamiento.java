package com.La_Salle.Colegio.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "convenio_fraccionamiento")
@Getter @Setter
public class ConvenioFraccionamiento {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_convenio")
    private Long id;

    @ManyToOne(optional = false) @JoinColumn(name = "id_matricula") private Matricula matricula;

    @Column(name = "deuda_total", nullable = false, precision = 10, scale = 2) private BigDecimal deudaTotal;
    @Column(name = "nro_cuotas", nullable = false) private int nroCuotas;
    @Column(name = "fecha_acuerdo", nullable = false) private LocalDate fechaAcuerdo;
    @Column(nullable = false, length = 20) private String estado;        // APROBADO
}
