package com.adri.api_spa.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DtoServicio {


    private String detallesServicio;

    private int duracionMinutos;  // Duración en minutos

    private double precio;

    private boolean disponible;

}
