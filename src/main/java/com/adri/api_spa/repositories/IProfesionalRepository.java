package com.adri.api_spa.repositories;


import com.adri.api_spa.models.Profesional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface IProfesionalRepository extends JpaRepository<Profesional, Long> {
//
//
//    @Query("SELECT p FROM Profesional p LEFT JOIN p.turnos t " +
//            "WHERE (t IS NULL OR (t.fecha != :fecha OR t.horaInicio != :horaInicio OR t.horaFin != :horaFin))")
//    List<Profesional> findProfesionalesDisponibles(@Param("fecha") LocalDate fecha,
//                                                   @Param("horaInicio") LocalTime horaInicio,
//                                                   @Param("horaFin") LocalTime horaFin);
}