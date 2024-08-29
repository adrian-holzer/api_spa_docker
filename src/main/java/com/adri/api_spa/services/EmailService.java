package com.adri.api_spa.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;


    @Value("${spring.mail.username}")
    private String sender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }




    public void enviarRespuestaPorEmail(String destinatario, String asunto, String cuerpo) throws MessagingException {
        MimeMessage mensaje = this.mailSender.createMimeMessage();


        try {
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);
            System.out.println(destinatario);
            System.out.println(asunto);
            System.out.println(cuerpo);
            System.out.println(sender);

            helper.setTo(destinatario);
            helper.setSubject(asunto);
            helper.setText(cuerpo,true); // El segundo parámetro indica si el contenido es HTML
            helper.setFrom(sender);

            this.mailSender.send(mensaje);
        }catch (MessagingException e){

            throw new RuntimeException(e);
        }

    }
}