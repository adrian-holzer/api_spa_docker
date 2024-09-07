package com.adri.api_spa.controllers;


import com.adri.api_spa.Utils.ApiError;
import com.adri.api_spa.Utils.ResponseHandler;
import com.adri.api_spa.models.*;
import com.adri.api_spa.services.ComentarioService;
import com.adri.api_spa.services.ServicioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicio/")
public class RestControllerServicio {



    @Autowired
    ServicioService ss ;




    @GetMapping("listar")
    public ResponseEntity<?> listadoServicios(@RequestParam(required = false) String categoria) {

        if (categoria == null) {
            return ResponseHandler.generateResponse("Listado de servicios disponibles ",HttpStatus.OK,this.ss.listarServicios());
        }

       List<Servicio> cs  =   ss.listarServicioPorCategoria(categoria);

        if (cs==null)
        {
            return ResponseHandler.generateResponse("No se encontro la categoria con nombre " + categoria,HttpStatus.BAD_REQUEST,null);


        }
        return ResponseHandler.generateResponse("Listado de servicios para la categoria " + categoria,HttpStatus.OK,cs);
    }



    @GetMapping(value = "{idServicio}")
    public ResponseEntity<?> listarServicioPorId(@PathVariable Long idServicio) {


        Servicio servicio = ss.listarServicioPorId(idServicio);

        if (servicio == null) {

            return ResponseHandler.generateResponse("No existe el Servicio buscado",HttpStatus.BAD_REQUEST,servicio);
        }


        return ResponseHandler.generateResponse("Servicio con id " + idServicio,HttpStatus.OK,servicio);

    }





    @PostMapping(value = "crear")
    public ResponseEntity<?> crearServicio(@RequestParam Long idServicio) {



        return ResponseHandler.generateResponse("Servicio creado ",HttpStatus.OK,"");

    }




}
