package com.adri.api_spa.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class DtoEmpleo {

    @NotBlank(message = "El titulo es obligatorio")
    private String titulo;

    @NotBlank(message = "La descripcion es obligatoria")
    private String descripcion;

}

