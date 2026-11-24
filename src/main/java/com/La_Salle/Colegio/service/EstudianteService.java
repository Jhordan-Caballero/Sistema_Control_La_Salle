package com.La_Salle.Colegio.service;


import com.La_Salle.Colegio.dao.EstudianteDao;
import com.La_Salle.Colegio.model.Estudiante;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstudianteService {

    private final EstudianteDao dao;

    public EstudianteService(EstudianteDao dao) {
        this.dao = dao;
    }

    public Estudiante registrar(Estudiante e) {
        if (e.getCodigo() == null || e.getCodigo().isBlank())
            throw new IllegalArgumentException("El código es obligatorio");
        if (e.getDni() == null || !e.getDni().matches("\\d{8}"))
            throw new IllegalArgumentException("El DNI debe tener 8 dígitos");
        if (e.getNombres() == null || e.getNombres().isBlank())
            throw new IllegalArgumentException("Los nombres son obligatorios");
        if (e.getApellidos() == null || e.getApellidos().isBlank())
            throw new IllegalArgumentException("Los apellidos son obligatorios");
        if (e.getFechaNacimiento() == null)
            throw new IllegalArgumentException("La fecha de nacimiento es obligatoria");

        // save() hace INSERT porque el id todavía es null
        return dao.save(e);
    }
    public List<Estudiante> listar() {
        return dao.findAll();
    }
}