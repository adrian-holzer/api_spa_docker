package com.adri.api_spa.dtos;

import com.adri.api_spa.models.Usuarios;
import lombok.Data;

//Esta clase va a ser la que nos devolverá la información con el token y el tipo que tenga este
@Data
public class DtoAuthRespuesta {
    private String accessToken;
    private String tokenType = "Bearer ";
    private Usuarios usuarioLogueado;

    public DtoAuthRespuesta(String accessToken,Usuarios usuarioLogueado) {
        this.accessToken = accessToken;
        this.usuarioLogueado=usuarioLogueado;
    }
}
