package com.adri.api_spa.repositories;


import com.adri.api_spa.models.CategoriaServicio;
import com.adri.api_spa.models.Comentario;
import com.adri.api_spa.models.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICategoriaServicioRepository extends JpaRepository<CategoriaServicio, Long> {


    CategoriaServicio findByNombre(String nombre);

}
