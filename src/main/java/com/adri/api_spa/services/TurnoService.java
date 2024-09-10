package com.adri.api_spa.services;

import com.adri.api_spa.models.*;
import com.adri.api_spa.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class TurnoService {

    @Autowired
    ITurnoRepository turnoRepository;

    @Autowired
    IClienteRepository clienteRepository;


    @Autowired
    IServicioRepository servicioRepository;


    @Autowired
    IHorarioLaboralRepository horarioLaboralRepository;

    @Autowired
    IProfesionalRepository profesionalRepository;

    @Autowired
    public TurnoService(ITurnoRepository turnoRepo) {
        this.turnoRepository = turnoRepo;
    }

    //Creamos un turno
    public void crear(Turno turno) {
        turnoRepository.save(turno);
    }

    //Obtenemos toda una lista de turnos
    public List<Turno> readAll() {
        return turnoRepository.findAll();
    }

    //Obtenemos un turno por su id
    public Optional<Turno> readOne(Long id) {
        return turnoRepository.findById(id);
    }

    //Actualizamos un turno
    public void update(Turno turno) {
        turnoRepository.save(turno);
    }

    //Eliminamos un turno
    public void delete(Long id) {
        turnoRepository.deleteById(id);
    }


    public void crearTurno(Long idServicio , Long idCliente, LocalDate fecha, LocalTime horaInicio){

//        Cliente cliente = clienteRepository.findById(idCliente)
//                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
//        Servicio servicio = servicioRepository.findById(idServicio)
//                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
//
//        // Obtengo el dia de la semana
//
//        String dia = fecha.getDayOfWeek()
//                .getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
//
//        // creo una lista con los profesionales disponibles ese dia
//
//        Turno t = new Turno();
//
//        t.setCliente(cliente);
//        //t.getProfesionales().add();
//        t.setEstado(EstadoTurno.EN_PROCESO);
//        t.setServicio(servicio);
//        t.setHoraInicio(horaInicio);
//        t.setHoraFin(t.getHoraInicio().plusMinutes(servicio.getDuracionMinutos()));
//
//
//


    }


    public void filtrarHorasDisponibles(Long idServicio, LocalDate fechaDeseada){

        // Hacer consulta todos los profesionales que se encuentren ese dia y que no tengan turnos asignados con estado en proceso

        Servicio servicio = servicioRepository.findById(idServicio)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        String dia = fechaDeseada.getDayOfWeek()
                .getDisplayName(TextStyle.FULL, new Locale("es", "ES"));

      //  HorarioLaboral hl = horarioLaboralRepository.


    }

}
