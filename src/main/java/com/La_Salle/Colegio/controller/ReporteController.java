package com.La_Salle.Colegio.controller;

import com.La_Salle.Colegio.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService service;

    @GetMapping("/resumen")
    public Map<String, Object> resumen() {
        return service.resumen();
    }
}