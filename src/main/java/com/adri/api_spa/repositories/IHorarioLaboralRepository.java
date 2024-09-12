package com.adri.api_spa.repositories;


import com.adri.api_spa.models.DiaSemana;
import com.adri.api_spa.models.HorarioLaboral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IHorarioLaboralRepository extends JpaRepository<HorarioLaboral, Long> {

    List<HorarioLaboral> findByDiaSemana(DiaSemana diaSemana);




}
