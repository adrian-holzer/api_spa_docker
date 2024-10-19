package com.adri.api_spa.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "turno")
public class Turno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTurno;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    // Relación Many-to-Many con Servicio

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "turno_servicio",
            joinColumns = @JoinColumn(name = "id_turno"),
            inverseJoinColumns = @JoinColumn(name = "id_servicio")
    )
    private Set<Servicio> servicios = new HashSet<>();



//    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinTable(
//            name = "turno_profesional",
//            joinColumns = @JoinColumn(name = "id_turno"),
//            inverseJoinColumns = @JoinColumn(name = "id_profesional"))
//    private Set<Profesional> profesionales = new HashSet<>();


    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_profesional")
    @JsonManagedReference
    private Profesional profesional;

    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;

    @OneToOne(mappedBy = "turno")
    @JsonBackReference
    private Pago pago;  // El turno tiene un pago


    @Enumerated(EnumType.STRING)
    private EstadoTurno estado;


    private LocalDateTime fechaAsignacionTurno;

//
//    @PrePersist
//    protected void onCreate() {
//        this.fechaCreacionTurno = LocalDateTime.now();
//    }


}
