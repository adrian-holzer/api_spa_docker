package com.adri.api_spa.controllers;


import com.adri.api_spa.Utils.ResponseHandler;
import com.adri.api_spa.dtos.ClienteDto;
import com.adri.api_spa.models.*;
import com.adri.api_spa.repositories.IClienteRepository;
import com.adri.api_spa.repositories.ITurnoRepository;
import com.adri.api_spa.repositories.IUsuariosRepository;
import com.adri.api_spa.services.ClienteService;
import com.adri.api_spa.services.EmpleoService;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
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
    private IUsuariosRepository usuariosRepository;

    @Autowired
    private ITurnoRepository turnoRepository;


    // - Listado de todos los clientes registrados

    @GetMapping("/listar")
    public ResponseEntity<?> getClientes() {

        // Llamamos al repositorio para obtener los usuarios con rol "CLIENTE"
        List<Usuarios> clientes = usuariosRepository.findAllByRole("CLIENTE");


        List<ClienteDto> clienteDTOs = clientes.stream()
                .map(cliente -> new ClienteDto(
                        cliente.getCliente().getIdCliente(),
                        cliente.getCliente().getTelefono(),
                        cliente.getCliente().getDomicilio(),
                        cliente.getUsername(),
                        cliente.getNombre(),
                        cliente.getApellido(),
                        cliente.getDni(),
                        cliente.getEmail()
                ))
                .collect(Collectors.toList());

        return ResponseHandler.generateResponse("Listado clientes", HttpStatus.OK, clienteDTOs);



    }





}
