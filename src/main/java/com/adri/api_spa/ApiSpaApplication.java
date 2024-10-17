package com.adri.api_spa;

import com.adri.api_spa.repositories.IRolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.management.relation.Role;

@SpringBootApplication
@EnableScheduling  // Habilita la programación de tareas
public class ApiSpaApplication {





	public static void main(String[] args) {
		SpringApplication.run(ApiSpaApplication.class, args);





	}

}
