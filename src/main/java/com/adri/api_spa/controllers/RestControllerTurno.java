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
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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


// Solo el profesional o admin

    @GetMapping(value = "listar")
    public ResponseEntity<?> listarTurnos(@RequestParam(required = false) String estado) {


        if (estado!=null){

            if (estado.equalsIgnoreCase("asignado")){

               List<Turno> listadoTurnos =  turnoRepository.findByEstado(EstadoTurno.ASIGNADO);
                return ResponseHandler.generateResponse("Listados de turnos asignados  " , HttpStatus.OK,listadoTurnos);


            }
            else if (estado.equalsIgnoreCase("finalizado")){
                List<Turno> listadoTurnos =  turnoRepository.findByEstado(EstadoTurno.FINALIZADO);
                return ResponseHandler.generateResponse("Listados de turnos finalizados  " , HttpStatus.OK,listadoTurnos);


            }
            else if (estado.equalsIgnoreCase("cancelado")){
                List<Turno> listadoTurnos =  turnoRepository.findByEstado(EstadoTurno.CANCELADO);

                return ResponseHandler.generateResponse("Listados de turnos cancelados  " , HttpStatus.OK,listadoTurnos);

            }else {

                return ResponseHandler.generateResponse("No se encontraron turnos con ese estado", HttpStatus.BAD_REQUEST,null);
            }
        }
        else {
            List<Turno> listaTurnos = turnoRepository.findAll();
            return ResponseHandler.generateResponse("Listados de turnos" , HttpStatus.OK,listaTurnos);
        }




    }

// Solo profesionales y admin
    @GetMapping("/por-fecha")
    public ResponseEntity<?> getTurnosPorFecha(@RequestParam("fecha") String fecha) {


        try {

            // Parsear el String a LocalDate
            LocalDate fechaDada = LocalDate.parse(fecha);
            // Buscar turnos por la fecha
            List<Turno> listadoTurnos = turnoRepository.findByFecha(fechaDada);
            return ResponseHandler.generateResponse("Listados de turnos para la fecha " +  fecha , HttpStatus.OK,listadoTurnos);
        }catch (DateTimeParseException e) {
            // Si la fecha está malformada, devolver BadRequest con un mensaje
            return new ResponseEntity<>("Formato de fecha incorrecto. Use 'YYYY-MM-DD'", HttpStatus.BAD_REQUEST);
        }


    }



//  para cliente, profesional y admin

    @GetMapping("/disponibles")
    public ResponseEntity<Map<String, Object>> obtenerHorariosDisponibles(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam Long servicioId) {
        Map<String, Object> horarios = turnoService.obtenerHorariosDisponiblesAuto(servicioId,fecha);




        return ResponseEntity.ok(horarios);
    }


    // para el cliente
    @GetMapping("/misTurnos")
    public ResponseEntity<?> obtenerMisTurnos() {

        Cliente c = clienteRepository.findById(usuarioService.usuarioLogueado().getCliente().getIdCliente()).get();

        List<Turno>turnosCliente = turnoRepository.findByCliente(c);



        return ResponseHandler.generateResponse("Listados de turnos para el cliente " + c.getUsuario().getUsername(), HttpStatus.OK,turnosCliente);

    }


// solo profesional y admin

    @GetMapping(value = "cliente/{idCliente}")
    public ResponseEntity<?> obtenerTurnosPorCliente(@PathVariable Long idCliente) {

        Cliente c = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));



        List<Turno>turnosCliente = turnoRepository.findByCliente(c);



        return ResponseHandler.generateResponse("Listados de turnos para el cliente " + c.getUsuario().getUsername(), HttpStatus.OK,turnosCliente);

    }


// para el cliente
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



    //  para cliente que pueda cancelar su propio turno


    @DeleteMapping("/cancelar/{idTurno}")
    public ResponseEntity<?> cancelarTurno(@PathVariable Long idTurno) {

        Usuarios usuarioLogueado = usuarioService.usuarioLogueado();


        Turno turno = turnoRepository.findById(idTurno).isPresent() ? turnoRepository.findById(idTurno).get()  : null ;

        if (turno==null){
            return ResponseHandler.generateResponse("No se encontro el turno con id " + idTurno, HttpStatus.BAD_REQUEST,null);

        }
        if (usuarioLogueado.getCliente() != null ){
                       if (turno.getCliente().getIdCliente()==usuarioLogueado.getCliente().getIdCliente()){
                           turno.setEstado(EstadoTurno.CANCELADO);
                           turnoRepository.save(turno);
                           return ResponseHandler.generateResponse("Se ha cancelado el turno con id " + idTurno, HttpStatus.OK,turno);

                       }
                       else {

                           return ResponseHandler.generateResponse("No puede modificar un turno que no le pertenece", HttpStatus.OK,turno);

                       }
        }


        if (usuarioLogueado.getProfesional() != null ){

                turno.setEstado(EstadoTurno.CANCELADO);
                turnoRepository.save(turno);
            return ResponseHandler.generateResponse("Se ha cancelado el turno con id " + idTurno, HttpStatus.OK,turno);

        }


            return ResponseHandler.generateResponse("No puede modificar un turno que no le pertenece", HttpStatus.OK,turno);



    }

// Solo para profesionales o admin
    @PutMapping("/finalizarTurno/{idTurno}")
    public ResponseEntity<?> finalizarTurno(@RequestParam Long idTurno) {

        Turno turno = turnoRepository.findById(idTurno).isPresent() ? turnoRepository.findById(idTurno).get()  : null ;

        if (turno==null){
            return ResponseHandler.generateResponse("No se encontro el turno con id " + idTurno, HttpStatus.BAD_REQUEST,null);

        }


        turno.setEstado(EstadoTurno.CANCELADO);
        turnoRepository.save(turno);



        return ResponseHandler.generateResponse("Se ha cancelado el turno con id " + idTurno, HttpStatus.OK,turno);


    }



}
