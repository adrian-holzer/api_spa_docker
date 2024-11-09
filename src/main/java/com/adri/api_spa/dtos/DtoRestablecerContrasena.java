package com.adri.api_spa.dtos;


import lombok.Data;

@Data
public class DtoRestablecerContrasena {


    private String token;
    private String nuevaContrasena;

    // Getters y Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNuevaContrasena() {
        return nuevaContrasena;
    }

    public void setNuevaContrasena(String nuevaContrasena) {
        this.nuevaContrasena = nuevaContrasena;
    }
}
