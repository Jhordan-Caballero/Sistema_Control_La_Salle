package com.La_Salle.Colegio.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reporte_bancario")
@Getter @Setter
public class ReporteBancario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reporte")
    private Long id;

    @Column(nullable = false, length = 30) private String banco;
    @Column(name = "nombre_archivo", nullable = false, unique = true, length = 200) private String nombreArchivo;
    @Column(name = "fecha_carga", nullable = false) private LocalDateTime fechaCarga;
    @Column(nullable = false, length = 50) private String usuario;
}
