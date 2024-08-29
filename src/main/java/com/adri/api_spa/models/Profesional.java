package com.adri.api_spa.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "profesional")
public class Profesional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_profesional")
    private Long idProfesional;




    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id" , referencedColumnName = "id_usuario")
    @JsonBackReference
    private Usuarios usuario;








}
