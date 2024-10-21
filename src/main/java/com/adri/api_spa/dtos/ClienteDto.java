package com.adri.api_spa.dtos;


import lombok.Data;

@Data
public class ClienteDto {

    private Long idCliente;
    private String telefono;
    private String domicilio;
    private String nombreUsuario;
    private String nombre;
    private String apellido;
    private String dni;
    private String email;
    public ClienteDto(Long idCliente, String telefono, String domicilio, String nombreUsuario,String nombre, String apellido, String dni, String email) {
        this.idCliente = idCliente;
        this.telefono = telefono;
        this.domicilio = domicilio;
        this.nombreUsuario = nombreUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
    }

    // Getters y setters
}