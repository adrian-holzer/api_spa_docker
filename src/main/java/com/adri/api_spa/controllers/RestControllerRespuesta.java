package com.adri.api_spa.controllers;

import com.adri.api_spa.models.Respuesta;
import com.adri.api_spa.services.RespuestaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/respuesta/")
public class RestControllerRespuesta {



    @Autowired
    private  RespuestaService respuestaService;



}