package com.La_Salle.Colegio.service;

import com.La_Salle.Colegio.dao.CuotaDao;
import com.La_Salle.Colegio.dao.MatriculaDao;
import com.La_Salle.Colegio.dao.PagoDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private final PagoDao pagoDao;
    private final CuotaDao cuotaDao;
    private final MatriculaDao matriculaDao;

    public Map<String, Object> resumen() {
        return Map.of(
                "totalRecaudado", pagoDao.sumMontos(),
                "deudaPendiente", cuotaDao.sumSaldo(),
                "matriculados", matriculaDao.count());
    }
}