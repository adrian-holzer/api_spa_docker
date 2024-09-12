package com.adri.api_spa.repositories;

import com.adri.api_spa.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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

}
