package com.adri.api_spa.services;

import com.adri.api_spa.models.DiaSemana;
import com.adri.api_spa.models.HorarioLaboral;
import com.adri.api_spa.models.Profesional;
import com.adri.api_spa.repositories.IHorarioLaboralRepository;
import com.adri.api_spa.repositories.IProfesionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Arrays;

@Service
public class ProfesionalService {

    @Autowired
    private IProfesionalRepository profesionalRepository;

    @Autowired
    private IHorarioLaboralRepository horarioLaboralRepository;

    public void asignarHorariosLaborales(Long idProfesional) {
        Profesional profesional = profesionalRepository.findById(idProfesional)
                .orElseThrow(() -> new RuntimeException("Profesional no encontrado"));

        HorarioLaboral horarioManana = new HorarioLaboral();
        horarioManana.setProfesional(profesional);
        horarioManana.setDiaSemana(DiaSemana.LUNES);  // Puedes asignar el día que corresponda
        horarioManana.setHoraInicio(LocalTime.of(8, 0));
        horarioManana.setHoraFin(LocalTime.of(12, 0));

        HorarioLaboral horarioTarde = new HorarioLaboral();
        horarioTarde.setProfesional(profesional);
        horarioTarde.setDiaSemana(DiaSemana.LUNES);  // Puedes asignar el día que corresponda
        horarioTarde.setHoraInicio(LocalTime.of(17, 0));
        horarioTarde.setHoraFin(LocalTime.of(21, 0));

        horarioLaboralRepository.saveAll(Arrays.asList(horarioManana, horarioTarde));
    }
}