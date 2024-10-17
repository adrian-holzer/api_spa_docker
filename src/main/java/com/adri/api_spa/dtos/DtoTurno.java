package com.adri.api_spa.dtos;

import com.adri.api_spa.models.Cliente;
import com.adri.api_spa.models.Servicio;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class DtoTurno {

    private Long idProfesional;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fecha;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime horaInicio;




}
