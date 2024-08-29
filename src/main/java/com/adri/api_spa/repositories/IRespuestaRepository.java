package com.adri.api_spa.repositories;


import com.adri.api_spa.models.Cliente;
import com.adri.api_spa.models.Respuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRespuestaRepository extends JpaRepository<Respuesta, Long> {
}
