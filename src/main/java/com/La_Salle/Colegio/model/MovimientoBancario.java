package com.La_Salle.Colegio.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "movimiento_bancario")
@Getter @Setter
public class MovimientoBancario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimiento")
    private Long id;

    @ManyToOne(optional = false) @JoinColumn(name = "id_reporte") private ReporteBancario reporte;

    @Column(name = "codigo_estudiante", nullable = false, length = 20) private String codigoEstudiante;
    @Column(name = "nro_operacion", nullable = false, length = 50) private String nroOperacion;
    @Column(name = "fecha_movimiento", nullable = false) private LocalDate fechaMovimiento;
    @Column(nullable = false, precision = 10, scale = 2) private BigDecimal monto;
    @Column(name = "estado_conciliacion", nullable = false, length = 20) private String estadoConciliacion; // PENDIENTE, CONCILIADO, DIFERENCIA
    @Column(length = 255) private String observacion;
}