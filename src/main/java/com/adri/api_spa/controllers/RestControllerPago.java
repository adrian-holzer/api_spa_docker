package com.adri.api_spa.controllers;

import com.adri.api_spa.Utils.ResponseHandler;
import com.adri.api_spa.dtos.DtoPago;
import com.adri.api_spa.models.Cliente;
import com.adri.api_spa.models.Pago;
import com.adri.api_spa.models.Turno;
import com.adri.api_spa.models.Usuarios;
import com.adri.api_spa.repositories.ITurnoRepository;
import com.adri.api_spa.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/pago")
public class RestControllerPago {

    @Autowired
    PagoService pagoService;


    @Autowired
    UsuarioService usuarioService;

    @Autowired
    ITurnoRepository turnoRepository;



    @Autowired
    EmailService emailService;

    @Value("${spring.mail.host}")
    String host ;

    @Value("${spring.mail.port}")
    String port ;

    @Value("${spring.mail.username}")
    String username ;

    @Value("${spring.mail.password}")
    String password ;

    @PostMapping("/procesar")
    public ResponseEntity<?> procesarPago(@Validated @RequestBody DtoPago pagoDTO) {
        try {


            Turno turno = turnoRepository.findById(pagoDTO.getTurnoId())
                    .orElseThrow(() -> new RuntimeException("Turno con ID " + pagoDTO.getTurnoId() + " no encontrado"));


            if (turno.getCliente()== null || !usuarioService.usuarioLogueado().getCliente().getIdCliente().equals(turno.getCliente().getIdCliente())){

                return ResponseHandler.generateResponse("El turno no le pertenece al cliente logueado", HttpStatus.OK, null);

            }

            Pago pago = pagoService.procesarPago(pagoDTO);
            return ResponseHandler.generateResponse("Pago realizado exitosamente", HttpStatus.OK, pago);
        } catch (RuntimeException e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }



    // Ruta para recibir la factura y enviarla por email
    @PostMapping("/enviar-factura")
    public ResponseEntity<?> enviarFactura(@RequestParam("factura") MultipartFile factura) {
        try {
            Usuarios usuarioLogueado = usuarioService.usuarioLogueado();
            emailService.enviarFacturaPorEmail(usuarioLogueado.getEmail(), factura,host,port,username,password);

            return ResponseEntity.ok("Factura enviada correctamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error enviando la factura: " + e.getMessage());
        }
    }






}
