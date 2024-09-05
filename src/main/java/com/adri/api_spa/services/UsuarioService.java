package com.adri.api_spa.services;


import com.adri.api_spa.Utils.ResponseHandler;
import com.adri.api_spa.models.Usuarios;
import com.adri.api_spa.repositories.IUsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private IUsuariosRepository usuariosRepository;



    public Usuarios usuarioLogueado() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {


            Usuarios usuarioLogueado= usuariosRepository.findByUsername(authentication.getName()).get();

            return usuarioLogueado;
        }
        return null;

    }


    public Usuarios findByUsername(String username) {


        return this.usuariosRepository.findByUsername(username).get();

    }



}
