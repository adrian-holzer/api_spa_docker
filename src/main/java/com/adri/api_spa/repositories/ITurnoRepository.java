package com.adri.api_spa.repositories;

import com.adri.api_spa.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ITurnoRepository extends JpaRepository<Turno, Long> {

    List<Turno> findByFecha(LocalDate fecha);

    // Método para buscar turnos por fecha, cliente, horaInicio y estado
    List<Turno> findByFechaAndClienteAndHoraInicioAndEstado(
            LocalDate fecha,
            Cliente cliente,
            LocalTime horaInicio,
            EstadoTurno estado
    );


    List<Turno> findByCliente(Cliente cliente);

    List<Turno> findByEstado(EstadoTurno estado);

//
//    List<Turno> findByProfesionalAndOrderByHoraInicioAsc(
//            Profesional profesional
//
//    );



    Optional<Turno> findByProfesionalAndFechaAndHoraInicioAndEstadoNot(
            Profesional profesional, LocalDate fecha, LocalTime horaInicio, EstadoTurno estado);

    List<Turno> findByEstadoAndPagoIsNull(EstadoTurno estado);

    List<Turno> findByProfesionalAndFechaAndEstadoNot(Profesional profesional, LocalDate fecha, EstadoTurno estado);

}
