package com.adri.api_spa.repositories;


import com.adri.api_spa.models.Comentario;
import com.adri.api_spa.models.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IServicioRepository  extends JpaRepository<Servicio, Long>  {
}
