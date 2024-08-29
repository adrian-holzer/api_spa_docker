package com.adri.api_spa;

import com.adri.api_spa.models.Cliente;
import com.adri.api_spa.models.Profesional;
import com.adri.api_spa.models.Roles;
import com.adri.api_spa.models.Usuarios;
import com.adri.api_spa.repositories.IRolesRepository;
import com.adri.api_spa.repositories.IUsuariosRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;


@Component
public class DataInitializer implements CommandLineRunner {

    private final IRolesRepository roleRepository;
    private final IUsuariosRepository usuariosRepository;
    private PasswordEncoder passwordEncoder;

    public DataInitializer(IRolesRepository roleRepository,IUsuariosRepository usuariosRepository,PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.usuariosRepository= usuariosRepository;
        this.passwordEncoder= passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (roleRepository.findByName("CLIENTE").isEmpty()) {
            Roles userRole = new Roles();
            userRole.setName("CLIENTE");
            roleRepository.save(userRole);
        }

        if (roleRepository.findByName("ADMIN").isEmpty()) {
            Roles adminRole = new Roles();
            adminRole.setName("ADMIN");
            roleRepository.save(adminRole);
        }

        if (roleRepository.findByName("PROFESIONAL").isEmpty()) {
            Roles adminRole = new Roles();
            adminRole.setName("PROFESIONAL");
            roleRepository.save(adminRole);
        }





        Usuarios usuarios = new Usuarios();
        Profesional profesional = new Profesional();
        usuarios.setUsername("user_admin");
        usuarios.setPassword(passwordEncoder.encode("adminpass"));
        usuarios.setNombre("jorge");
        usuarios.setApellido("perez");
        usuarios.setDni("15555555");
        usuarios.setEmail("admin@email.com");
        usuarios.setProfesional(profesional);
        profesional.setUsuario(usuarios);

        // Obtener los roles del repositorio
        Roles adminRole = roleRepository.findByName("ADMIN").orElseThrow(() -> new RuntimeException("Rol ADMIN no encontrado"));
        Roles profesionalRole = roleRepository.findByName("PROFESIONAL").orElseThrow(() -> new RuntimeException("Rol PROFESIONAL no encontrado"));

        // Asignar ambos roles al usuario
        usuarios.setRoles(Arrays.asList(adminRole, profesionalRole));


        usuariosRepository.save(usuarios);





    }
}