package com.adri.api_spa.repositories;

import com.adri.api_spa.models.HorarioLaboral;
import com.adri.api_spa.models.Profesional;
import com.adri.api_spa.models.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ITurnoRepository extends JpaRepository<Turno, Long> {


//
//    @Query("SELECT * FROM Turno t where t.fecha=fecha and estado='EN PROCESO'")
//    List<Turno> findTurnosByFechaAndEstadoEnProceso(@Param("fecha") LocalDate fecha);
//


}
