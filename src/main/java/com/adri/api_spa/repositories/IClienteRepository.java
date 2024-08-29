package com.adri.api_spa.repositories;

import com.adri.api_spa.models.Cliente;
import com.adri.api_spa.models.Usuarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IClienteRepository  extends JpaRepository<Cliente, Long> {



}