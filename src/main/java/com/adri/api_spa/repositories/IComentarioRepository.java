package com.adri.api_spa.repositories;


import com.adri.api_spa.models.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IComentarioRepository  extends JpaRepository<Comentario, Long> {


      List<Comentario> findAllByOrderByCreatedAtDesc();
}
