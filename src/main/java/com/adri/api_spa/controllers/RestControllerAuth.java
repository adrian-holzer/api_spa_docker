package com.adri.api_spa.controllers;

import com.adri.api_spa.Utils.ApiError;
import com.adri.api_spa.Utils.ResponseHandler;
import com.adri.api_spa.dtos.DtoAuthRespuesta;
import com.adri.api_spa.dtos.DtoLogin;
import com.adri.api_spa.dtos.DtoRegistro;
import com.adri.api_spa.models.Cliente;
import com.adri.api_spa.models.Profesional;
import com.adri.api_spa.models.Roles;
import com.adri.api_spa.models.Usuarios;
import com.adri.api_spa.repositories.IClienteRepository;
import com.adri.api_spa.repositories.IRolesRepository;
import com.adri.api_spa.repositories.IUsuariosRepository;
import com.adri.api_spa.security.JwtGenerador;
import com.adri.api_spa.services.ProfesionalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/auth/")
public class RestControllerAuth {
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private IRolesRepository rolesRepository;
    private IClienteRepository clienteRepository;
    private IUsuariosRepository usuariosRepository;
    private JwtGenerador jwtGenerador;

    @Autowired
    ProfesionalService profesionalService;

    @Autowired
    public RestControllerAuth(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, IRolesRepository rolesRepository, IUsuariosRepository usuariosRepository,
                              IClienteRepository clienteRepository,JwtGenerador jwtGenerador) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.rolesRepository = rolesRepository;
        this.usuariosRepository = usuariosRepository;
        this.clienteRepository= clienteRepository;
        this.jwtGenerador = jwtGenerador;
    }
    //Método para poder registrar usuarios con role "cliente"
    @PostMapping("registerCliente")
    public ResponseEntity<?>  registrar(@Valid @RequestBody DtoRegistro dtoRegistro, Errors errors) {

        if (usuariosRepository.existsByUsername(dtoRegistro.getUsername())) {
            return new ResponseEntity<>("el usuario ya existe, intenta con otro", HttpStatus.BAD_REQUEST);
        }
        if (usuariosRepository.existsByEmail(dtoRegistro.getEmail())) {
            return new ResponseEntity<>("el email ya existe, intenta con otro", HttpStatus.BAD_REQUEST);
        }

        if (usuariosRepository.existsByDni(dtoRegistro.getDni())) {
            return new ResponseEntity<>("el dni ya existe, intenta con otro", HttpStatus.BAD_REQUEST);
        }

        if (errors.hasErrors()){

            return ResponseHandler.generateResponse("Complete los datos correctamente",HttpStatus.BAD_REQUEST,new ApiError(errors).getErrores());

        }
        Usuarios usuarios = new Usuarios();
        Cliente cliente =  new Cliente();
        cliente.setDomicilio(dtoRegistro.getDomicilio());
        cliente.setTelefono(dtoRegistro.getTelefono());
        usuarios.setUsername(dtoRegistro.getUsername());
        usuarios.setPassword(passwordEncoder.encode(dtoRegistro.getPassword()));
        usuarios.setNombre(dtoRegistro.getNombre());
        usuarios.setApellido(dtoRegistro.getApellido());
        usuarios.setEmail(dtoRegistro.getEmail());
        usuarios.setDni(dtoRegistro.getDni());

        usuarios.setCliente(cliente);
        cliente.setUsuario(usuarios);

        usuarios.setPassword(passwordEncoder.encode(dtoRegistro.getPassword()));

        Roles roles = rolesRepository.findByName("CLIENTE").get();
        usuarios.setRoles(Collections.singletonList(roles));

        usuariosRepository.save(usuarios);

        Usuarios u = usuariosRepository.findFirstByOrderByIdUsuarioDesc();

        return ResponseHandler.generateResponse("Usuario Cliente creado correctamente",HttpStatus.OK,u);

    }




    //Método para poder guardar usuarios de tipo ADMIN
    @PostMapping("registerAdm")
    public ResponseEntity<String> registrarAdmin(@RequestBody DtoRegistro dtoRegistro) {
        if (usuariosRepository.existsByUsername(dtoRegistro.getUsername())) {


            return new ResponseEntity<>("el usuario ya existe, intenta con otro", HttpStatus.BAD_REQUEST);
        }
        Usuarios usuarios = new Usuarios();
        usuarios.setUsername(dtoRegistro.getUsername());
        usuarios.setPassword(passwordEncoder.encode(dtoRegistro.getPassword()));
        Roles roles = rolesRepository.findByName("ADMIN").get();
        usuarios.setRoles(Collections.singletonList(roles));
        usuariosRepository.save(usuarios);
        return new ResponseEntity<>("Registro de admin exitoso", HttpStatus.OK);
    }


    //Método para poder guardar usuarios de tipo Profesionales

    @PostMapping("registerProf")
    public ResponseEntity<?> registrarProfesional(@Valid @RequestBody DtoRegistro dtoRegistro, Errors errors) {
        if (usuariosRepository.existsByUsername(dtoRegistro.getUsername())) {
            return new ResponseEntity<>("el usuario ya existe, intenta con otro", HttpStatus.BAD_REQUEST);
        }
        if (usuariosRepository.existsByEmail(dtoRegistro.getEmail())) {
            return new ResponseEntity<>("el email ya existe, intenta con otro", HttpStatus.BAD_REQUEST);
        }

        if (usuariosRepository.existsByDni(dtoRegistro.getDni())) {
            return new ResponseEntity<>("el dni ya existe, intenta con otro", HttpStatus.BAD_REQUEST);
        }

        if (errors.hasErrors()){

            return ResponseHandler.generateResponse("Complete los datos correctamente",HttpStatus.BAD_REQUEST,new ApiError(errors).getErrores());

        }
        Usuarios usuarios = new Usuarios();
        Profesional profesional =  new Profesional();






        usuarios.setUsername(dtoRegistro.getUsername());
        usuarios.setPassword(passwordEncoder.encode(dtoRegistro.getPassword()));
        usuarios.setNombre(dtoRegistro.getNombre());
        usuarios.setApellido(dtoRegistro.getApellido());
        usuarios.setEmail(dtoRegistro.getEmail());
        usuarios.setDni(dtoRegistro.getDni());

        usuarios.setProfesional(profesional);
        profesional.setUsuario(usuarios);

        usuarios.setPassword(passwordEncoder.encode(dtoRegistro.getPassword()));

        Roles roles = rolesRepository.findByName("PROFESIONAL").get();
        usuarios.setRoles(Collections.singletonList(roles));

        profesionalService.asignarHorarios(profesional);

        usuariosRepository.save(usuarios);

        // Busco el ultimo usuario creado para mandarlo en la confirmacion

        Usuarios u = usuariosRepository.findFirstByOrderByIdUsuarioDesc();








        return ResponseHandler.generateResponse("Usuario Profesional creado correctamente",HttpStatus.OK,u);
    }




    //Método para poder guardar usuarios de tipo Profesionales

    @PostMapping("registerSecretario")
    public ResponseEntity<?> registrarSecretario(@Valid @RequestBody DtoRegistro dtoRegistro, Errors errors) {


        if (usuariosRepository.existsByUsername(dtoRegistro.getUsername())) {


            return new ResponseEntity<>("el usuario ya existe, intenta con otro", HttpStatus.BAD_REQUEST);
        }

        Usuarios usuarios = new Usuarios();
        usuarios.setUsername(dtoRegistro.getUsername());
        usuarios.setPassword(passwordEncoder.encode(dtoRegistro.getPassword()));
        usuarios.setNombre(dtoRegistro.getNombre());
        usuarios.setApellido(dtoRegistro.getApellido());
        usuarios.setEmail(dtoRegistro.getEmail());
        usuarios.setDni(dtoRegistro.getDni());
        Roles roles = rolesRepository.findByName("SECRETARIO").get();
        usuarios.setRoles(Collections.singletonList(roles));
        usuariosRepository.save(usuarios);

        return new ResponseEntity<>("Registro de Secretario exitoso", HttpStatus.OK);
    }




    //Método para poder logear un usuario y obtener un token
    @PostMapping("login")
    public ResponseEntity<DtoAuthRespuesta> login(@RequestBody DtoLogin dtoLogin) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                dtoLogin.getUsername(), dtoLogin.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerador.generarToken(authentication);
       String username = jwtGenerador.obtenerUsernameDeJwt(token);

        Usuarios usuarioLogueado= usuariosRepository.findByUsername(username).get();


        return new ResponseEntity<>(new DtoAuthRespuesta(token,usuarioLogueado), HttpStatus.OK);
    }


    // Obtener usuario logueado

    @GetMapping("/userLogueado")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {


            Usuarios usuarioLogueado= usuariosRepository.findByUsername(authentication.getName()).get();

             return ResponseHandler.generateResponse("Usuario Logueado",HttpStatus.OK,usuarioLogueado);
        }
         return ResponseHandler.generateResponse("No existe usuario logueado",HttpStatus.BAD_REQUEST,null);
    }





//    Listado de clientes -




}
