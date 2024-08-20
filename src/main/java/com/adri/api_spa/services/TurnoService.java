package com.adri.api_spa.services;

import com.adri.api_spa.models.Turno;
import com.adri.api_spa.repositories.ITurnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TurnoService {
    private ITurnoRepository turnoRepo;

    @Autowired
    public TurnoService(ITurnoRepository turnoRepo) {
        this.turnoRepo = turnoRepo;
    }

    //Creamos un turno
    public void crear(Turno turno) {
        turnoRepo.save(turno);
    }

    //Obtenemos toda una lista de turnos
    public List<Turno> readAll() {
        return turnoRepo.findAll();
    }

    //Obtenemos un turno por su id
    public Optional<Turno> readOne(Long id) {
        return turnoRepo.findById(id);
    }

    //Actualizamos un turno
    public void update(Turno turno) {
        turnoRepo.save(turno);
    }

    //Eliminamos un turno
    public void delete(Long id) {
        turnoRepo.deleteById(id);
    }
}
