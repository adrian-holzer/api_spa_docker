package com.adri.api_spa.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class DtoConsulta {

    private Long idConsulta;

    @NotBlank(message = "El nombre de la persona es obligatorio")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    private String nombrePersona;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 255, message = "El título no puede tener más de 255 caracteres")
    private String temaConsulta;

    @NotBlank(message = "El detalle es obligatorio")
    @Size(max = 2000, message = "El detalle no puede tener más de 2000 caracteres")
    private String textoConsulta;


    private boolean estadoConsulta;

    @Email(message = "El email debe tener un formato válido")
    @NotBlank(message = "El email es obligatorio")
    private String email;

    }