package com.adri.api_spa.repositories;

import com.adri.api_spa.models.Empleo;
import com.adri.api_spa.models.EstadoEmpleo;
import com.adri.api_spa.models.EstadoTurno;
import com.adri.api_spa.models.Turno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IEmpleoRepository extends JpaRepository<Empleo, Long> {



    List<Empleo> findByEstado(EstadoEmpleo estado);}
