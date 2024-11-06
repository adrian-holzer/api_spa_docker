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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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


// Obtener datos de cliente logueado


    @GetMapping("/clienteLogueado")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {


            Usuarios usuarioLogueado= usuariosRepository.findByUsername(authentication.getName()).get();


            ClienteDto clienteDto = new ClienteDto(

                    usuarioLogueado.getCliente().getIdCliente(),
                    usuarioLogueado.getCliente().getTelefono(),
                    usuarioLogueado.getCliente().getDomicilio(),
                    usuarioLogueado.getUsername(),
                    usuarioLogueado.getNombre(),
                    usuarioLogueado.getApellido(),
                    usuarioLogueado.getDni(),
                    usuarioLogueado.getEmail()
            );

            return ResponseHandler.generateResponse("Cliente Logueado",HttpStatus.OK,clienteDto);
        }
        return ResponseHandler.generateResponse("No existe usuario logueado",HttpStatus.BAD_REQUEST,null);
    }


// Modificar datos de cliente


    @PutMapping("/modificar")
    public ResponseEntity<?> modificarCliente(@RequestBody ClienteDto clienteDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        System.out.println(clienteDto);
        if (authentication != null && authentication.isAuthenticated()) {
            Optional<Usuarios> usuarioAutenticadoOpt = usuariosRepository.findByUsername(authentication.getName());

            if (usuarioAutenticadoOpt.isPresent()) {
                Usuarios usuario = usuarioAutenticadoOpt.get();
                Cliente cliente = usuario.getCliente();

                // Actualizar datos del cliente
                cliente.setTelefono(clienteDto.getTelefono());
                cliente.setDomicilio(clienteDto.getDomicilio());

                // Actualizar datos del usuario
                usuario.setUsername(clienteDto.getNombreUsuario());
                usuario.setNombre(clienteDto.getNombre());
                usuario.setApellido(clienteDto.getApellido());
                usuario.setDni(clienteDto.getDni());
                usuario.setEmail(clienteDto.getEmail());

                // Guardar cambios en el repositorio
                usuariosRepository.save(usuario);

                return ResponseHandler.generateResponse("Cliente actualizado con éxito", HttpStatus.OK, new ClienteDto(
                        cliente.getIdCliente(),
                        cliente.getTelefono(),
                        cliente.getDomicilio(),
                        usuario.getUsername(),
                        usuario.getNombre(),
                        usuario.getApellido(),
                        usuario.getDni(),
                        usuario.getEmail()
                ));
            } else {
                return ResponseHandler.generateResponse("Usuario no encontrado", HttpStatus.NOT_FOUND, null);
            }
        }

        return ResponseHandler.generateResponse("No existe usuario logueado", HttpStatus.UNAUTHORIZED, null);
    }




}
