package com.adri.api_spa.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuario")
public class Usuarios {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;
    private String username;


    @JsonIgnore
    private String password;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío")
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
    private String apellido;

    @NotBlank(message = "El DNI no puede estar vacío")
    @Pattern(regexp = "^\\d{8}$", message = "El DNI debe tener 8 dígitos , sin puntos.")
    @Column(unique = true) // Define DNI as unique in the database
    private String dni;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email debe tener un formato válido")
    @Column(unique = true) // Define email as unique in the database
    private String email;


    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Cliente cliente;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Profesional profesional;





    //Usamos fetchType en EAGER para que cada vez que se acceda o se extraiga un usuario de la BD, este se traiga todos sus roles
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    /*Con JoinTable estaremos creando una tabla que unirá la tabla de usuario y role, con lo cual tendremos un total de 3 tablas
    relacionadas en la tabla "usuarios_roles", a través de sus columnas usuario_id que apuntara al ID de la tabla usuario
    y role_id que apuntara al Id de la tabla role */
    @JoinTable(name = "usuarios_roles", joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id_usuario")
            ,inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id_role"))
    private List<Roles> roles = new ArrayList<>();







    }
