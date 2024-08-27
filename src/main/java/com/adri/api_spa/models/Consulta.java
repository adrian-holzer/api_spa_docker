package com.adri.api_spa.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "consulta")
public class Consulta {


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id_consulta")
        private Long idConsulta;
        private String nombrePersona;
        private String temaConsulta;
        private String textoConsulta;
        private boolean estadoConsulta;

        @Email(message = "El email debe tener un formato válido")
        @NotBlank(message = "El email es obligatorio")
        private String email;














}
