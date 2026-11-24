package com.La_Salle.Colegio.controller;

import com.La_Salle.Colegio.model.Matricula;
import com.La_Salle.Colegio.service.MatriculaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matriculas")
@RequiredArgsConstructor
public class MatriculaController {

    private final MatriculaService service;

    public record MatriculaRequest(Long estudianteId, Long periodoId, Long seccionId) {}

    @PostMapping
    public ResponseEntity<Matricula> matricular(@RequestBody MatriculaRequest r) {
        Matricula m = service.matricular(r.estudianteId(), r.periodoId(), r.seccionId());
        return ResponseEntity.status(HttpStatus.CREATED).body(m);
    }
    @GetMapping
    public List<Matricula> listar() {
        return service.listar();
    }
}