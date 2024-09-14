package com.adri.api_spa.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "postulacion")
public class Postulacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_postulacion")
    private Long idPostulacion;

    @ManyToOne
    @JoinColumn(name = "id_empleo", nullable = false)
    private Empleo empleo;
    @Column(name = "cv_filename")
    private String cvFileName; // Aquí se almacena el nombre del archivo

    @Lob
    @Column(name = "cv", nullable = false,length = 100000)
    private byte[] cv; // Aquí se almacena el archivo PDF como un array de bytes

//    @Column(name = "cv_file_name")
//    private String cvFileName; // Almacena el nombre del archivo PDF

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


}
