package com.adri.api_spa.controllers;

import com.adri.api_spa.Utils.ApiError;
import com.adri.api_spa.Utils.ResponseHandler;
import com.adri.api_spa.dtos.DtoEmpleo;
import com.adri.api_spa.models.Empleo;
import com.adri.api_spa.models.EstadoEmpleo;
import com.adri.api_spa.models.Postulacion;
import com.adri.api_spa.models.Servicio;
import com.adri.api_spa.services.EmpleoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/empleo")
public class RestControllerEmpleo {

    @Autowired
    private EmpleoService empleoService;



    @GetMapping("/{id}/postulaciones")
    public ResponseEntity<?> getPostulacionesByEmpleoId(@PathVariable Long id) {
        Optional<Empleo> empleoOptional = empleoService.getEmpleoWithPostulaciones(id);

        if (empleoOptional.isPresent()) {
            Empleo empleo = empleoOptional.get();
            List<Postulacion> postulaciones = empleo.getPostulaciones();



            return ResponseEntity.ok(postulaciones);
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    @GetMapping("/listar")
    public ResponseEntity<?> listarEmpleos(@RequestParam(required = false) String estado) {


        if (estado==null) {
            List<Empleo> empleos = empleoService.listarEmpleos();
            return ResponseHandler.generateResponse("Listado de todos los Empleos",HttpStatus.OK,empleos);
        }
        else {
            if (estado.equalsIgnoreCase("activo")){

                List<Empleo> empleos = empleoService.listarEmpleosPorEstado(EstadoEmpleo.ACTIVO);

                return ResponseHandler.generateResponse("Listado de todos los Empleos "+  EstadoEmpleo.ACTIVO.name() ,HttpStatus.OK,empleos);

            }
           else if (estado.equalsIgnoreCase("inactivo")){

                List<Empleo> empleos = empleoService.listarEmpleosPorEstado(EstadoEmpleo.INACTIVO);
                return ResponseHandler.generateResponse("Listado de todos los Empleos " +  EstadoEmpleo.ACTIVO.name(),HttpStatus.OK,empleos);


            }
           else {
                return ResponseHandler.generateResponse("Estado no valido ",HttpStatus.BAD_REQUEST,null);

            }
        }






    }



    @PostMapping("/crear")
    public ResponseEntity<?> crearEmpleo(@Valid @RequestBody DtoEmpleo dtoEmpleo, Errors errors) {


        if (errors.hasErrors()){

            return ResponseHandler.generateResponse("Complete los datos correctamente",HttpStatus.BAD_REQUEST,new ApiError(errors).getErrores());

        }

        Empleo empleo = new Empleo();

        empleo.setTitulo(dtoEmpleo.getTitulo() );
        empleo.setDescripcion(dtoEmpleo.getDescripcion() );
 empleoService.guardarEmpleo(empleo);

   return ResponseHandler.generateResponse("Empleo creado ", HttpStatus.CREATED,empleo);

    }




}