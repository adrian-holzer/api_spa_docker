package com.adri.api_spa.controllers;


import com.adri.api_spa.Utils.ApiError;
import com.adri.api_spa.Utils.ResponseHandler;
import com.adri.api_spa.dtos.DtoTurno;
import com.adri.api_spa.models.*;
import com.adri.api_spa.repositories.IClienteRepository;
import com.adri.api_spa.repositories.IProfesionalRepository;
import com.adri.api_spa.repositories.IServicioRepository;
import com.adri.api_spa.repositories.ITurnoRepository;
import com.adri.api_spa.services.TurnoService;
import com.adri.api_spa.services.UsuarioService;
import com.mysql.cj.xdevapi.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
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
    UsuarioService usuarioService;

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
    public ResponseEntity<?> crearTurno(@RequestBody DtoTurno turnoDto) {


        Turno nuevoTurno =  new Turno();


        nuevoTurno.setServicio(servicioRepository.findById(Long.parseLong(turnoDto.getId_servicio())).get());

        nuevoTurno.setEstado(EstadoTurno.ASIGNADO);

        nuevoTurno.setCliente(usuarioService.usuarioLogueado().getCliente());

        nuevoTurno.setHoraInicio(turnoDto.getHoraInicio());
        nuevoTurno.setHoraFin(turnoDto.getHoraFin());
        nuevoTurno.setFecha(turnoDto.getFecha());






        // Busco profesional disponible, los pongo en una lista y asigno random

       Profesional profesional =  turnoService.obtenerProfesionalDisponible(turnoDto.getFecha(),turnoDto.getHoraInicio(),turnoDto.getHoraFin());

       if (profesional== null){
           return ResponseHandler.generateResponse("No hay personal disponible en este momento", HttpStatus.BAD_REQUEST,null);

       }


        nuevoTurno.setProfesional(profesional);

       turnoRepository.save(nuevoTurno);

        return  ResponseEntity.ok(nuevoTurno);

    }



}
