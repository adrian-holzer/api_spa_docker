package com.adri.api_spa.dtos;

import lombok.Data;

@Data
public class DtoRecuperarContrasena {


    private String email;

    // Getters y Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
