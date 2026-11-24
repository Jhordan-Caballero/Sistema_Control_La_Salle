package com.La_Salle.Colegio.controller;

import com.La_Salle.Colegio.service.EstadoCuentaService;
import com.La_Salle.Colegio.service.EstadoCuentaService.EstadoCuenta;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/estudiantes")
@RequiredArgsConstructor
public class EstadoCuentaController {

    private final EstadoCuentaService service;

    @GetMapping("/{id}/estado-cuenta")
    public EstadoCuenta estadoCuenta(@PathVariable Long id) {
        return service.obtener(id);
    }
}