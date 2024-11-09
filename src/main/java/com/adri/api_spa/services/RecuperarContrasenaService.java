package com.adri.api_spa.services;
import com.adri.api_spa.models.Usuarios;
import com.adri.api_spa.repositories.IUsuariosRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class RecuperarContrasenaService {

    @Value("${spring.mail.host}")
    String host ;

    @Value("${spring.mail.port}")
    String port ;

    @Value("${spring.mail.username}")
    String username ;

    @Value("${spring.mail.password}")
    String password ;

    @Autowired
    private IUsuariosRepository usuarioRepository;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private EmailService mailService;


    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean generarToken(String email) {
        Usuarios usuario = usuarioRepository.findByEmail(email).orElse(null);

        if (usuario == null) {
            return false;
        }

        String token = UUID.randomUUID().toString();
        usuario.setTokenRecuperacion(token);
        usuario.setTokenExpiracion(LocalDateTime.now().plusHours(1));
        usuarioRepository.save(usuario);

        try {
            mailService.enviarCorreoRecuperacion(email, token,host,port,username,password);

        } catch (MessagingException e) {
            return false;
        }

        return true;
    }

    public boolean restablecerContrasena(String token, String nuevaContrasena) {
        Usuarios usuario = usuarioRepository.findByTokenRecuperacion(token).orElse(null);
        if (usuario == null || usuario.getTokenExpiracion().isBefore(LocalDateTime.now())) {
            return false;
        }

       usuario.setPassword(passwordEncoder.encode(nuevaContrasena));
        usuario.setTokenRecuperacion(null);
        usuario.setTokenExpiracion(null);
        usuarioRepository.save(usuario);
        return true;
    }
}