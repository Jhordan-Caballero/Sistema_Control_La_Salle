package com.La_Salle.Colegio.controller;

import com.La_Salle.Colegio.model.ReporteBancario;
import com.La_Salle.Colegio.service.ConciliacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/conciliacion")
@RequiredArgsConstructor
public class ConciliacionController {

    private final ConciliacionService service;

    @PostMapping(value = "/reportes", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReporteBancario> importar(@RequestParam String banco,
                                                    @RequestParam MultipartFile archivo,
                                                    @RequestParam(defaultValue = "tesorero") String usuario) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.importarCsv(banco, archivo, usuario));
    }

    @PostMapping("/reportes/{id}/conciliar")
    public Map<String, Object> conciliar(@PathVariable Long id) {
        return service.conciliar(id);
    }
}