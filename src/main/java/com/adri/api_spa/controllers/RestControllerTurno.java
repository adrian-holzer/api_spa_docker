package com.adri.api_spa.controllers;

import com.adri.api_spa.models.Turno;
import com.adri.api_spa.services.TurnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/turno/")
public class RestControllerTurno {
    private TurnoService turnoService;

    @Autowired
    public RestControllerTurno(TurnoService phoneService) {
        this.turnoService = phoneService;
    }

    @PostMapping(value = "crear", headers = "Accept=application/json")
    public void crearCelular(@RequestBody Turno turno) {
        turnoService.crear(turno);
    }

    //Petición para obtener todos los turnos en la BD
    @GetMapping(value = "listar", headers = "Accept=application/json")
    public List<Turno> listarCelulares() {
        return turnoService.readAll();
    }

    @GetMapping(value = "listarId/{id}", headers = "Accept=application/json")
    public Optional<Turno> obtenerCelularPorId(@PathVariable Long id) {
        return turnoService.readOne(id);
    }

    @PutMapping(value = "actualizar", headers = "Accept=application/json")
    public void actualizarCelular(@RequestBody Turno smartPhone) {
        turnoService.update(smartPhone);
    }

    @DeleteMapping(value = "eliminar/{id}", headers = "Accept=application/json")
    public void eliminarCelular(@PathVariable Long id) {
        turnoService.delete(id);
    }
}
