package com.adri.api_spa.repositories;


import com.adri.api_spa.models.HorarioLaboral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IHorarioLaboralRepository extends JpaRepository<HorarioLaboral, Long> {
}
