package com.adri.api_spa.services;

import com.adri.api_spa.models.DiaSemana;
import com.adri.api_spa.models.HorarioLaboral;
import com.adri.api_spa.models.Profesional;

import com.adri.api_spa.repositories.IHorarioLaboralRepository;
import com.adri.api_spa.repositories.IProfesionalRepository;
import org.hibernate.mapping.Array;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;

@Service
public class ProfesionalService {


    @Autowired
    IProfesionalRepository profesionalRepository;

    @Autowired
    IHorarioLaboralRepository horarioLaboralRepository;

    public void asignarHorarios(Profesional p) {


        // Buscar horarios
        List<HorarioLaboral> lh = horarioLaboralRepository.findAll();

        // Asignar los horarios al profesional
        p.setHorariosLaborales(lh);

    }
}