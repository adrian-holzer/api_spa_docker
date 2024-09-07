package com.adri.api_spa.services;


import com.adri.api_spa.dtos.DtoServicio;
import com.adri.api_spa.models.CategoriaServicio;
import com.adri.api_spa.models.Comentario;
import com.adri.api_spa.models.Servicio;
import com.adri.api_spa.repositories.ICategoriaServicioRepository;
import com.adri.api_spa.repositories.IComentarioRepository;
import com.adri.api_spa.repositories.IServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioService {



    @Autowired
    private IServicioRepository servicioRepository;


    @Autowired
    private ICategoriaServicioRepository categoriaServicioRepository;


    public Servicio crearServicio(Servicio servicio) {

        return this.servicioRepository.save(servicio);

    }

    public List<Servicio> listarServicios() {

        return this.servicioRepository.findAll();

    }

    public Servicio listarServicioPorId(Long id_servicio) {


        return servicioRepository.findById(id_servicio).orElse(null);


    }


    public List<Servicio> listarServicioPorCategoria(String nombreCategoriaServicio) {

       CategoriaServicio c = this.categoriaServicioRepository.findByNombre(nombreCategoriaServicio);

      return  c== null ? null :  this.categoriaServicioRepository.findByNombre(nombreCategoriaServicio).getServicios();

    }

}
