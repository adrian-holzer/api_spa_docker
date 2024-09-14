package com.adri.api_spa.services;

import com.adri.api_spa.models.Empleo;
import com.adri.api_spa.models.EstadoEmpleo;
import com.adri.api_spa.repositories.IEmpleoRepository;
import com.adri.api_spa.repositories.IPostulacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmpleoService {

    @Autowired
    private IEmpleoRepository empleoRepository;

    @Autowired
    private IPostulacionRepository postulacionRepository;

    public Optional<Empleo> getEmpleoWithPostulaciones(Long empleoId) {
        return empleoRepository.findById(empleoId);
    }


    public Empleo guardarEmpleo(Empleo empleo) {
        return empleoRepository.save(empleo);
    }


    public List<Empleo> listarEmpleos() {
        return empleoRepository.findAll();
    }


    public List<Empleo> listarEmpleosPorEstado(EstadoEmpleo estado) {
        return empleoRepository.findByEstado(estado);
    }
}