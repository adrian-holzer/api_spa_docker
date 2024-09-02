package com.adri.api_spa.services;

import com.adri.api_spa.models.Comentario;
import com.adri.api_spa.models.Consulta;
import com.adri.api_spa.repositories.IComentarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComentarioService {



    @Autowired
    private IComentarioRepository comentarioRepository;


    public Comentario crearComentario(Comentario comentario) {

        return this.comentarioRepository.save(comentario);

    }


    public List<Comentario> findAllByOrderByCreatedAtDesc() {

        return this.comentarioRepository.findAllByOrderByCreatedAtDesc();

    }
}
