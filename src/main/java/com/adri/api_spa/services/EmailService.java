package com.adri.api_spa.services;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Properties;

@Service
public class EmailService {

    private final JavaMailSender mailSender;


    @Value("${spring.mail.username}")
    private String sender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    public void sendPlainTextEmail(String host, String port,
                                   final String userName, final String password, String toAddress,
                                   String subject, String message) throws AddressException,
            MessagingException {

        // sets SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
// *** BEGIN CHANGE
        properties.put("mail.smtp.user", userName);

        // creates a new session, no Authenticator (will connect() later)
        Session session = Session.getDefaultInstance(properties);
// *** END CHANGE

        // creates a new e-mail message
        Message msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress(userName));
        InternetAddress[] toAddresses = { new InternetAddress(toAddress) };
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject(subject);
        msg.setSentDate(new Date());
        // set plain text message
        msg.setText(message);

// *** BEGIN CHANGE
        // sends the e-mail
        Transport t = session.getTransport("smtp");
        t.connect(userName, password);
        t.sendMessage(msg, msg.getAllRecipients());
        t.close();
// *** END CHANGE

    }

    public void enviarRespuestaPorEmail(String destinatario, String asunto, String cuerpo) throws MessagingException {
        MimeMessage mensaje = this.mailSender.createMimeMessage();
        SimpleMailMessage sm = new SimpleMailMessage();
        sm.setFrom(this.sender);
        sm.setSubject("hoa");
        sm.setText("hola2");
        sm.setTo("prueba.metod.2@gmail.com");
        System.out.println("destinatario");
        System.out.println(asunto);
        System.out.println(cuerpo);
        System.out.println(sender);
        System.out.println("fin");

        this.mailSender.send(sm);
        System.out.println("fin2");
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);
            System.out.println(destinatario);
            System.out.println(asunto);
            System.out.println(cuerpo);
            System.out.println(sender);
//
//            helper.setTo(destinatario);
//            helper.setSubject(asunto);
//            helper.setText(cuerpo,true); // El segundo parámetro indica si el contenido es HTML
//            helper.setFrom(sender);
//
//            this.mailSender.send(mensaje);
        }catch (MessagingException e){

            throw new RuntimeException(e);
        }

    }
}