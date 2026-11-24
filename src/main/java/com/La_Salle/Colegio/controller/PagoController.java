package com.La_Salle.Colegio.controller;

import com.La_Salle.Colegio.model.Pago;
import com.La_Salle.Colegio.service.PagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService service;

    public record PagoRequest(Long cuotaId, BigDecimal monto, String medioPago,
                              String nroOperacion, boolean confirmarExtemporaneo, String usuario) {}

    @PostMapping
    public ResponseEntity<Pago> registrar(@RequestBody PagoRequest r) {
        Pago p = service.registrar(r.cuotaId(), r.monto(), r.medioPago(), r.nroOperacion(),
                r.confirmarExtemporaneo(), r.usuario());
        return ResponseEntity.status(HttpStatus.CREATED).body(p);
    }
}