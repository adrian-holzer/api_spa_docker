package com.adri.api_spa.services;

import com.adri.api_spa.models.Respuesta;
import com.adri.api_spa.repositories.IRespuestaRepository;
import com.adri.api_spa.repositories.IUsuariosRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class RespuestaService {

    @Value("${spring.mail.host}")
    String host ;

    @Value("${spring.mail.port}")
    String port ;

    @Value("${spring.mail.username}")
    String username ;

    @Value("${spring.mail.password}")
    String password ;

    private  EmailService emailService;


    private  IRespuestaRepository respuestaRepository;


    private  IUsuariosRepository usuariosRepository;

    public RespuestaService(EmailService emailService, IRespuestaRepository respuestaRepository, IUsuariosRepository usuariosRepository) {
        this.emailService = emailService;
        this.respuestaRepository = respuestaRepository;
        this.usuariosRepository = usuariosRepository;
    }

    public Respuesta crearYEnviarRespuesta(Respuesta respuesta, String emailDestinatario) {


        // Obtener el usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Verificar que el usuario tiene el rol de "PROFESIONAL"
        if (!userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("PROFESIONAL") || a.getAuthority().equals("ADMIN") )) {
            throw new SecurityException("Usuario no autorizado para responder consultas");
        }


        respuesta.setProfesional(this.usuariosRepository.findByUsername(userDetails.getUsername()).get().getProfesional());




        respuesta.getConsulta().setContestado(true);

        // Guardar la respuesta en la base de datos
        Respuesta respuestaGuardada = this.respuestaRepository.save(respuesta);



        // Crear el contenido del correo
        String asunto = "Respuesta a tu consulta:  " + respuesta.getConsulta().getTemaConsulta()+ "\n";
        String cuerpo = "Hola " + respuesta.getConsulta().getNombrePersona() + ",\n\n"
                + "Aquí está la respuesta a tu consulta:\n\n"
                + respuesta.getTextoRespuesta() + "\n\n"
                + "Saludos,\n"
                + "El equipo del Spa Sentirse Bien";


        // Enviar la respuesta por email
        try {
            this.emailService.sendPlainTextEmail(host,port,username
                    ,password, emailDestinatario,asunto,cuerpo ); //enviarRespuestaPorEmail(emailDestinatario, asunto, cuerpo);
        } catch (MessagingException e) {


            // Manejar la excepción de envío de correo
            e.printStackTrace();
        }



        return respuestaGuardada;
    }
}