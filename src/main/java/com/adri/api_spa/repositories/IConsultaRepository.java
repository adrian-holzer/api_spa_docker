package com.adri.api_spa.repositories;

import com.adri.api_spa.models.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface IConsultaRepository extends JpaRepository<Consulta, Long> {


    Optional<Consulta> findByIdConsulta(Long idConsulta);

    List<Consulta> findByContestado(boolean contestado);


    }

