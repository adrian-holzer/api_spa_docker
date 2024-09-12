package com.adri.api_spa.controllers;

import com.adri.api_spa.Utils.ApiError;
import com.adri.api_spa.Utils.ResponseHandler;
import com.adri.api_spa.dtos.DtoConsulta;
import com.adri.api_spa.models.Comentario;
import com.adri.api_spa.models.Consulta;
import com.adri.api_spa.services.ComentarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comentario/")
public class RestControllerComentario {


    @Autowired
    ComentarioService cs ;



    @PostMapping(value = "crear")
    public ResponseEntity<?> crearComentario(@Valid @RequestBody Comentario comentario, Errors errors) {

        if (errors.hasErrors()){

            return ResponseHandler.generateResponse("Complete los datos correctamente",HttpStatus.BAD_REQUEST,new ApiError(errors).getErrores());

        }

        Comentario c = new Comentario();
        c.setNombrePersona(comentario.getNombrePersona());
        c.setTextoComentario(comentario.getTextoComentario());

        cs.crearComentario(c);

        return ResponseHandler.generateResponse("Comentario Enviada",HttpStatus.CREATED,c);

    }


    @GetMapping(value = "listar")
    public ResponseEntity<?> listarComentarios() {

        return ResponseHandler.generateResponse("Listado de Comentarios",HttpStatus.OK,cs.findAllByOrderByCreatedAtDesc());

    }







}
