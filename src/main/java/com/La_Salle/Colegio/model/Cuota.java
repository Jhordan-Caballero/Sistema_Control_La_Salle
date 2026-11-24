package com.La_Salle.Colegio.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "cuota")
@Getter @Setter
public class Cuota {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cuota")
    private Long id;

    @ManyToOne(optional = false) @JoinColumn(name = "id_matricula") private Matricula matricula;

    @Column(nullable = false, length = 20) private String concepto;   // MATRICULA, PENSION, FRACCIONAMIENTO
    @Column(name = "nro_cuota", nullable = false) private int nroCuota;
    @Column(nullable = false, precision = 10, scale = 2) private BigDecimal monto;
    @Column(nullable = false, precision = 10, scale = 2) private BigDecimal saldo;
    @Column(name = "fecha_vencimiento", nullable = false) private LocalDate fechaVencimiento;
    @Column(nullable = false, length = 20) private String estado;     // PENDIENTE, PARCIAL, PAGADA, REFINANCIADA
}