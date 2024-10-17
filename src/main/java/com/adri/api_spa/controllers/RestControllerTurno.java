package com.adri.api_spa.controllers;


import com.adri.api_spa.Utils.ApiError;
import com.adri.api_spa.Utils.ResponseHandler;
import com.adri.api_spa.dtos.DtoAsignarTurno;
import com.adri.api_spa.dtos.DtoTurno;
import com.adri.api_spa.models.*;
import com.adri.api_spa.repositories.*;
import com.adri.api_spa.services.PagoService;
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
import java.util.Set;
import java.util.stream.Collectors;

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

    @Autowired
    IPagoRepository pagoRepository;

    @Autowired
    PagoService pagoService;



// Solo el profesional , secretario o admin


    @GetMapping(value = "listar")
    public ResponseEntity<?> listarTurnos(@RequestParam(required = false) String estado) {


        if (estado!=null){

            if (estado.equalsIgnoreCase("libre")){
                List<Turno> listadoTurnos =  turnoRepository.findByEstado(EstadoTurno.LIBRE);

                return ResponseHandler.generateResponse("Listados de turnos libres  " , HttpStatus.OK,listadoTurnos);


            }


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

    // Listar turnos por fecha (obtengo el cliente)

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





//  para cliente -- Todos los turnos disponibles

    @GetMapping("/disponibles")
    public ResponseEntity<?> obtenerHorariosDisponibles() {



        List<Turno> listadoTurnos =  turnoRepository.findByEstado(EstadoTurno.LIBRE);

        return ResponseHandler.generateResponse("Listados de turnos libres  " , HttpStatus.OK,listadoTurnos);


    }


    // para el cliente
    @GetMapping("/misTurnos")
    public ResponseEntity<?> obtenerMisTurnos(@RequestParam(required = false) Long idTurno) {


        Cliente c = clienteRepository.findById(usuarioService.usuarioLogueado().getCliente().getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // Si se pasa el idTurno, buscar un turno específico
        if (idTurno != null) {
            Optional<Turno> turno = turnoRepository.findById(idTurno);


            // Verificar si el turno pertenece al cliente logueado
            if (turno.isPresent() && turno.get().getCliente().getIdCliente().equals(c.getIdCliente())) {
                return ResponseHandler.generateResponse("Turno con ID: " + idTurno, HttpStatus.OK, turno.get());
            } else {
                return ResponseHandler.generateResponse("Turno no encontrado o no pertenece al cliente", HttpStatus.NOT_FOUND, null);
            }
        }




        List<Turno>turnosCliente = turnoRepository.findByCliente(c);

        return ResponseHandler.generateResponse("Listados de todos los turnos turnos del cliente " + c.getUsuario().getUsername(), HttpStatus.OK,turnosCliente);

    }


// solo profesional y admin

    @GetMapping(value = "cliente/{idCliente}")
    public ResponseEntity<?> obtenerTurnosPorCliente(@PathVariable Long idCliente) {

        Cliente c = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));



        List<Turno>turnosCliente = turnoRepository.findByCliente(c);



        return ResponseHandler.generateResponse("Listados de turnos para el cliente " + c.getUsuario().getUsername(), HttpStatus.OK,turnosCliente);

    }



    //  para cliente que pueda cancelar su propio turno


    @DeleteMapping("/cancelar/{idTurno}")
    public ResponseEntity<?> cancelarTurno(@PathVariable Long idTurno) {

        Usuarios usuarioLogueado = usuarioService.usuarioLogueado();


        Turno turno = turnoRepository.findById(idTurno).isPresent() ? turnoRepository.findById(idTurno).get()  : null ;

        if (turno==null){
            return ResponseHandler.generateResponse("No se encontro el turno con id " + idTurno, HttpStatus.BAD_REQUEST,null);

        }

        if (turno.getEstado().equals(EstadoTurno.LIBRE)){
            return ResponseHandler.generateResponse("No se puede cancelar un turno que no esta asignado " , HttpStatus.BAD_REQUEST,null);

        }
        if (usuarioLogueado.getCliente() != null ){
                       if (turno.getCliente().getIdCliente()==usuarioLogueado.getCliente().getIdCliente()){

                           if (turno.getPago()!=null){
                               return ResponseHandler.generateResponse("No se puede cancelar un turno pagado " , HttpStatus.BAD_REQUEST,turno);

                           }

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



// MODIFICACIONES





    // para el admin, profesional, secretaria --- Se crea el turno con el horario disponible estado no asignado
    @PostMapping("/crear")
    public ResponseEntity<?> crearTurno(@RequestBody DtoTurno turnoDto) {

        // Buscar el profesional por su ID
        Profesional profesional = profesionalRepository.findById(turnoDto.getIdProfesional())
                .orElseThrow(() -> new RuntimeException("Profesional no encontrado"));




        // Crear el turno con los datos proporcionados

        Turno turno = new Turno();
        turno.setProfesional(profesional);
        turno.setFecha(turnoDto.getFecha());
        turno.setHoraInicio(turnoDto.getHoraInicio());



        // Crear el turno en la base de datos
        Turno turnoCreado = turnoService.crearTurno(turno);

        // Retornar una respuesta exitosa con el turno creado
        return ResponseEntity.ok(turnoCreado);
    }



    @PostMapping("/asignar")
    public ResponseEntity<?> asignarTurno(
            @RequestBody DtoAsignarTurno asignarTurnoDto) {

        // Obtener el turno por ID
        Optional<Turno> optionalTurno = turnoRepository.findById(asignarTurnoDto.getIdTurno());
        if (!optionalTurno.isPresent()) {
            return ResponseHandler.generateResponse("Turno con ID " + asignarTurnoDto.getIdTurno() + " no encontrado",
                    HttpStatus.BAD_REQUEST, null);
        }

        Turno turno = optionalTurno.get();

        // Verificar si el turno ya está asignado a un cliente
        if (turno.getCliente() != null) {
            // Si ya tiene un cliente asignado, devolver el mensaje de advertencia
            return ResponseHandler.generateResponse("El turno ya está asignado a un cliente.",
                    HttpStatus.BAD_REQUEST, null);
        }

        // Obtener el cliente logueado
        Cliente cliente = usuarioService.usuarioLogueado().getCliente();

        // Buscar los servicios en la base de datos usando sus IDs
        Set<Servicio> servicios = asignarTurnoDto.getServicioIds().stream()
                .map(id -> servicioRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Servicio con ID " + id + " no encontrado")))
                .collect(Collectors.toSet());

        // Asignar el turno al cliente con los servicios seleccionados
        Turno turnoAsignado = turnoService.asignarTurno(asignarTurnoDto.getIdTurno(), cliente, servicios);

        // Retornar el turno asignado
        return ResponseEntity.ok(turnoAsignado);
    }





    //    @PostMapping("/procesar")
//    public ResponseEntity<?> procesarPago(@RequestBody DtoPago pagoDto) {
//        // Buscamos el turno por su ID
//        Turno turno = turnoService.readOne(pagoDto.getTurnoId()).get();
//
//        // Procesamos el pago
//        Pago pago = pagoService.crearPago(turno, pagoDto.getMetodoPago(), pagoDto.getMonto());
//
//        if ("Completado".equals(pago.getEstadoPago())) {
//            return ResponseEntity.ok("Pago completado y factura enviada");
//        } else {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al procesar el pago");
//        }
//    }



}
