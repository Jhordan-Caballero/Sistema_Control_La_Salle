package com.La_Salle.Colegio.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pago")
@Getter @Setter
public class Pago {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Long id;

    @ManyToOne(optional = false) @JoinColumn(name = "id_cuota") private Cuota cuota;
    @ManyToOne @JoinColumn(name = "id_movimiento") private MovimientoBancario movimiento;

    @Column(name = "fecha_pago", nullable = false) private LocalDateTime fechaPago;   // RNF05
    @Column(nullable = false, precision = 10, scale = 2) private BigDecimal monto;
    @Column(name = "medio_pago", length = 30) private String medioPago;              // VENTANILLA, BANCA_MOVIL, BANCO
    @Column(name = "nro_operacion", length = 50) private String nroOperacion;
    private boolean extemporaneo;
    @Column(nullable = false, length = 50) private String usuario;                   // RNF05
}
