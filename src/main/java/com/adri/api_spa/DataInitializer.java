package com.adri.api_spa;

import com.adri.api_spa.models.Roles;
import com.adri.api_spa.repositories.IRolesRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;



@Component
public class DataInitializer implements CommandLineRunner {

    private final IRolesRepository roleRepository;

    public DataInitializer(IRolesRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
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
    }
}