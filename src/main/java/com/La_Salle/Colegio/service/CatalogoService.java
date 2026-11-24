package com.La_Salle.Colegio.service;

import com.La_Salle.Colegio.dao.*;
import com.La_Salle.Colegio.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CatalogoService {

    private final ApoderadoDao apoderadoDao;
    private final GradoDao gradoDao;
    private final SeccionDao seccionDao;
    private final PeriodoAcademicoDao periodoDao;

    public Apoderado guardarApoderado(Apoderado a) { return apoderadoDao.save(a); }
    public Grado guardarGrado(Grado g) { return gradoDao.save(g); }
    public Seccion guardarSeccion(Seccion s) { return seccionDao.save(s); }
    public PeriodoAcademico guardarPeriodo(PeriodoAcademico p) { return periodoDao.save(p); }
    public List<Apoderado> listarApoderados() { return apoderadoDao.findAll(); }
    public List<Grado> listarGrados() { return gradoDao.findAll(); }
    public List<Seccion> listarSecciones() { return seccionDao.findAll(); }
    public List<PeriodoAcademico> listarPeriodos() { return periodoDao.findAll(); }
}