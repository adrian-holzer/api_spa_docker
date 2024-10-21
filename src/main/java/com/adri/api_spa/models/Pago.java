package com.adri.api_spa.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pago")
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "turno_id")
    private Turno turno;

    @Enumerated(EnumType.STRING)
    private MetodoPago metodoPago; // Débito o Crédito


    @NotNull
    private double monto;



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



    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime fechaPago;


    private EstadoPago estadoPago; // Pendiente, Pagado

    @PrePersist
    protected void onCreate() {
        this.fechaPago = LocalDateTime.now();
    }



}

