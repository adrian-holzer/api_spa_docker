package com.adri.api_spa.controllers;


import com.adri.api_spa.Utils.ResponseHandler;
import com.adri.api_spa.dtos.ClienteDto;
import com.adri.api_spa.models.Cliente;
import com.adri.api_spa.models.Empleo;
import com.adri.api_spa.models.Postulacion;
import com.adri.api_spa.models.Turno;
import com.adri.api_spa.repositories.IClienteRepository;
import com.adri.api_spa.repositories.ITurnoRepository;
import com.adri.api_spa.services.ClienteService;
import com.adri.api_spa.services.EmpleoService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cliente")
public class RestControllerCliente {


    @Autowired
    private ClienteService clienteService;
    @Autowired
    private IClienteRepository clienteRepository;

    @Autowired
    private ITurnoRepository turnoRepository;


    // - Listado de todos los clientes registrados

    @GetMapping("/listar")
    public ResponseEntity<?> getClientes() {

            List<Cliente> listadoClientes = clienteRepository.findAll();

        // Convertir a DTO
        List<ClienteDto> clienteDTOs = listadoClientes.stream()
                .map(cliente -> new ClienteDto(
                        cliente.getIdCliente(),
                        cliente.getTelefono(),
                        cliente.getDomicilio(),
                        cliente.getUsuario().getUsername(),
                        cliente.getUsuario().getNombre(),
                        cliente.getUsuario().getApellido(),
                        cliente.getUsuario().getDni(),
                        cliente.getUsuario().getEmail()
                ))
                .collect(Collectors.toList());

            return ResponseHandler.generateResponse("Listado clientes", HttpStatus.OK, clienteDTOs);

    }





}
