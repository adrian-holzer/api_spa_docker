package com.adri.api_spa.controllers;

import com.adri.api_spa.Utils.ApiError;
import com.adri.api_spa.Utils.ResponseHandler;
import com.adri.api_spa.dtos.DtoConsulta;
import com.adri.api_spa.models.Consulta;
import com.adri.api_spa.models.Respuesta;
import com.adri.api_spa.models.Usuarios;
import com.adri.api_spa.services.ConsultaService;
import com.adri.api_spa.services.RespuestaService;
import com.adri.api_spa.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/consulta/") // crear y responder
public class RestControllerConsulta {



    @Autowired
    private ConsultaService consultaService;
    @Autowired
    private RespuestaService respuestaService;
    @Autowired
    private UsuarioService usuarioService;



    @PostMapping(value = "crear")
    public ResponseEntity<?> crearConsulta(@Valid @RequestBody DtoConsulta dtoConsulta, Errors errors) {


        if (errors.hasErrors()){

            return ResponseHandler.generateResponse("Complete los datos correctamente",HttpStatus.BAD_REQUEST,new ApiError(errors).getErrores());

        }




        Consulta c = new Consulta();
        c.setNombrePersona(dtoConsulta.getNombrePersona());
        c.setEmail(dtoConsulta.getEmail());
        c.setTextoConsulta(dtoConsulta.getTextoConsulta());
        c.setTemaConsulta(dtoConsulta.getTemaConsulta());



        consultaService.crear(c);

        return ResponseHandler.generateResponse("Consulta Enviada",HttpStatus.CREATED,c);




    }





    @PostMapping(value = "{idConsulta}/respuestas/crear")
    public ResponseEntity<?> crearRespuesta(@RequestBody Respuesta respuesta,@PathVariable Long idConsulta) {

        Consulta consulta = this.consultaService.findById(idConsulta);

        if (consulta == null) {
            return ResponseHandler.generateResponse("No se encontró la consulta con id "+ idConsulta ,HttpStatus.BAD_REQUEST,null);
        }

        respuesta.setConsulta(consulta);

        Respuesta nuevaRespuesta = respuestaService.crearYEnviarRespuesta(
                respuesta, respuesta.getConsulta().getEmail()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaRespuesta);
    }



    @GetMapping(value = "{idConsulta}")
    public ResponseEntity<?> consultaPorId(@PathVariable Long idConsulta) {

        Consulta consulta = this.consultaService.findById(idConsulta);

        if (consulta == null) {
            return ResponseHandler.generateResponse("No se encontró la consulta con id "+ idConsulta ,HttpStatus.BAD_REQUEST,null);
        }



        return ResponseEntity.status(HttpStatus.OK).body(consulta);
    }


    @GetMapping(value = "listar")
    public ResponseEntity<?> listarConsultas(@RequestParam(required = false) Boolean contestado) {

      // Si no se envía el parámetro "contestado", devuelve todas las consultas
        if (contestado == null) {
            List<Consulta> consultas = this.consultaService.findAll();
             consultas = this.consultaService.findAll();
            return  ResponseEntity.status(HttpStatus.OK).body(consultas);
        }


        // Si se envía el parámetro "contestado", filtra las consultas según su estado
        List<Consulta> consultas =  consultaService.findByContestado(contestado);


        return ResponseEntity.status(HttpStatus.OK).body(consultas);

    }











}
