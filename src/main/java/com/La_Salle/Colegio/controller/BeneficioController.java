package com.La_Salle.Colegio.controller;

import com.La_Salle.Colegio.model.ConvenioFraccionamiento;
import com.La_Salle.Colegio.model.Descuento;
import com.La_Salle.Colegio.service.BeneficioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/beneficios")
@RequiredArgsConstructor
public class BeneficioController {

    private final BeneficioService service;

    public record BecaRequest(BigDecimal porcentaje, String motivo) {}
    public record FraccionRequest(int nroCuotas) {}

    @PostMapping("/hermanos/{matriculaId}")
    public Descuento hermanos(@PathVariable Long matriculaId) {
        return service.descuentoHermanos(matriculaId);
    }

    @PostMapping("/beca/{matriculaId}")
    public Descuento beca(@PathVariable Long matriculaId, @RequestBody BecaRequest r) {
        return service.beca(matriculaId, r.porcentaje(), r.motivo());
    }

    @PostMapping("/fraccionamiento/{matriculaId}")
    public ConvenioFraccionamiento fraccionar(@PathVariable Long matriculaId, @RequestBody FraccionRequest r) {
        return service.fraccionar(matriculaId, r.nroCuotas());
    }
}