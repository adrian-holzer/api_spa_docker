package com.adri.api_spa.controllers;


import com.adri.api_spa.models.EstadoTurno;
import com.adri.api_spa.models.Turno;
import com.adri.api_spa.repositories.IClienteRepository;
import com.adri.api_spa.repositories.IProfesionalRepository;
import com.adri.api_spa.repositories.IServicioRepository;
import com.adri.api_spa.repositories.ITurnoRepository;
import com.adri.api_spa.services.TurnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/turno/")
public class RestControllerTurno {


    @Autowired
    TurnoService turnoService;

    @Autowired
    IClienteRepository clienteRepository;

    @Autowired
    IServicioRepository servicioRepository;

    @Autowired
    IProfesionalRepository profesionalRepository;


    @Autowired
    ITurnoRepository turnoRepository;


    @GetMapping("/disponibles")
    public ResponseEntity<Map<String, Object>> obtenerHorariosDisponibles(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam Long servicioId) {
        Map<String, Object> horarios = turnoService.obtenerHorariosDisponiblesAuto(servicioId,fecha);
        return ResponseEntity.ok(horarios);
    }


    @PostMapping("/crear")
    public ResponseEntity<?> crearTurno(@RequestParam("clienteId") Long clienteId,
                                            @RequestParam("servicioId") Long servicioId,
                                            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
                                            @RequestParam("horaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime horaInicio,
                                        @RequestParam("horaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime horaFin) {

        Turno nuevoTurno =  new Turno();

//        nuevoTurno.setServicio();
//
//        nuevoTurno.setEstado(EstadoTurno.ASIGNADO);
//
//        nuevoTurno.setCliente();
//
//        nuevoTurno.setHoraInicio(horaInicio);
//        nuevoTurno.setHoraFin(horaFin);
//        nuevoTurno.setFecha(fecha);
//
//        // Busco profesional disponible, los pongo en una lista y asigno random
//
//        nuevoTurno.setProfesional();




        return ResponseEntity.ok("");

    }

}
