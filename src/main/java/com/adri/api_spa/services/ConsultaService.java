package com.adri.api_spa.services;


import com.adri.api_spa.models.Consulta;
import com.adri.api_spa.repositories.IConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConsultaService {

    @Autowired
    private IConsultaRepository consultaRepository;


    public Consulta crear(Consulta consulta) {

        return this.consultaRepository.save(consulta);

    }


    public Consulta findById(Long id) {

        return this.consultaRepository.findById(id).isPresent() ? this.consultaRepository.findById(id).get() : null;

    }



}
