package com.adri.api_spa.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private Usuarios usuario;


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "Profesional_HorarioLaboral",
            joinColumns = @JoinColumn(name = "id_profesional"),
            inverseJoinColumns = @JoinColumn(name = "id_horario_laboral")
    )
    @JsonBackReference
    private List<HorarioLaboral> horariosLaborales = new ArrayList<>();

    @OneToMany(mappedBy = "profesional",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Turno> turnos;



}
