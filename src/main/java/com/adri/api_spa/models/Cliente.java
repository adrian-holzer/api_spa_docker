package com.adri.api_spa.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Long idCliente;

    private String telefono;

    private String domicilio;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id" , referencedColumnName = "id_usuario")
    private Usuarios usuario;


    @OneToMany(mappedBy = "cliente", fetch = FetchType.EAGER)
    @JsonBackReference
    private List<Turno> turnos;


}
