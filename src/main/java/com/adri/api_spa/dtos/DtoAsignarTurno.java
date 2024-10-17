package com.adri.api_spa.dtos;

import java.util.Set;


public class DtoAsignarTurno {
    private Long idTurno;
    private Set<Long> servicioIds;

    // Getters y setters
    public Long getIdTurno() {
        return idTurno;
    }

    public void setIdTurno(Long idTurno) {
        this.idTurno = idTurno;
    }

    public Set<Long> getServicioIds() {
        return servicioIds;
    }

    public void setServicioIds(Set<Long> servicioIds) {
        this.servicioIds = servicioIds;
    }
}


