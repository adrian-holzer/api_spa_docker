package com.adri.api_spa.dtos;


import com.adri.api_spa.models.MetodoPago;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DtoPago {

    @NotNull
    private Long turnoId; // El ID del turno asociado

    @NotBlank
    @Size(min = 20, max = 20, message = "El número de tarjeta debe tener 20 dígitos")
    private String numTarjeta;

    @NotBlank
    private String nombreTitular;

    @NotBlank
    @Pattern(regexp = "(0[1-9]|1[0-2])\\/([0-9]{2})", message = "El vencimiento debe estar en formato MM/AA")
    private String vencimiento;

    @NotNull
    @Pattern(regexp = "\\d{3}", message = "El código de seguridad debe tener 3 dígitos")
    private String codSeguridad;

    @NotNull
    private MetodoPago metodoPago; // Débito o Crédito
}

