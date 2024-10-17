package com.adri.api_spa.services;


import com.adri.api_spa.repositories.IClienteRepository;
import com.adri.api_spa.repositories.IComentarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {



    @Autowired
    private IClienteRepository clienteRepository;




}
