package com.La_Salle.Colegio.controller;

import com.La_Salle.Colegio.model.*;
import com.La_Salle.Colegio.service.CatalogoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalogo")
@RequiredArgsConstructor
public class CatalogoController {

    private final CatalogoService service;

    @PostMapping("/apoderados")
    public ResponseEntity<Apoderado> apoderado(@RequestBody Apoderado a) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardarApoderado(a));
    }

    @PostMapping("/grados")
    public ResponseEntity<Grado> grado(@RequestBody Grado g) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardarGrado(g));
    }

    @PostMapping("/secciones")
    public ResponseEntity<Seccion> seccion(@RequestBody Seccion s) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardarSeccion(s));
    }

    @PostMapping("/periodos")
    public ResponseEntity<PeriodoAcademico> periodo(@RequestBody PeriodoAcademico p) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardarPeriodo(p));
    }
    @GetMapping("/apoderados")
    public List<Apoderado> apoderados() { return service.listarApoderados(); }

    @GetMapping("/grados")
    public List<Grado> grados() { return service.listarGrados(); }

    @GetMapping("/secciones")
    public List<Seccion> secciones() { return service.listarSecciones(); }

    @GetMapping("/periodos")
    public List<PeriodoAcademico> periodos() { return service.listarPeriodos(); }
}