package com.adri.api_spa.controllers;


import com.adri.api_spa.Utils.ResponseHandler;
import com.adri.api_spa.models.Cliente;
import com.adri.api_spa.models.Profesional;
import com.adri.api_spa.repositories.IProfesionalRepository;
import com.adri.api_spa.repositories.ITurnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/profesional")
public class RestControllerProfesional {


    @Autowired
    private IProfesionalRepository profesionalRepository;

    @GetMapping("/listar")
    public ResponseEntity<?> getProfesionales() {

        List<Profesional> listadoProfesionales = profesionalRepository.findAll();
        return ResponseHandler.generateResponse("Listado de todos los Profesionales", HttpStatus.OK, listadoProfesionales);

    }


}
