package com.adri.api_spa.repositories;


import com.adri.api_spa.models.Profesional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProfesionalRepository extends JpaRepository<Profesional, Long> {
}