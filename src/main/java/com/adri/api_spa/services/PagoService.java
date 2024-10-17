package com.adri.api_spa.services;

import com.adri.api_spa.dtos.DtoPago;
import com.adri.api_spa.models.*;
import com.adri.api_spa.repositories.IPagoRepository;
import com.adri.api_spa.repositories.ITurnoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Service
public class PagoService {


    @Value("${spring.mail.host}")
    String host ;

    @Value("${spring.mail.port}")
    String port ;

    @Value("${spring.mail.username}")
    String username ;

    @Value("${spring.mail.password}")
    String password ;

    private  EmailService emailService;

    private final ITurnoRepository turnoRepository;
    private final IPagoRepository pagoRepository;

    @Autowired
    UsuarioService usuarioService;


    public PagoService(ITurnoRepository turnoRepository, IPagoRepository pagoRepository) {
        this.turnoRepository = turnoRepository;
        this.pagoRepository = pagoRepository;
    }




    public Pago procesarPago(DtoPago pagoDTO) {
        // Verificar si el turno existe
        Turno turno = turnoRepository.findById(pagoDTO.getTurnoId())
                .orElseThrow(() -> new RuntimeException("Turno con ID " + pagoDTO.getTurnoId() + " no encontrado"));

        // Verificar si el turno ya tiene un pago asociado
        if (turno.getPago() != null) {
            throw new RuntimeException("Este turno ya tiene un pago registrado.");
        }



        // Calcular el monto total basado en los servicios asociados al turno
        double montoTotal = turno.getServicios().stream()
                .mapToDouble(Servicio::getPrecio)  // Convierte los precios a double
                .sum();  // Suma todos los precios

        // Crear el objeto Pago
        Pago pago = new Pago();
        pago.setTurno(turno);
        pago.setMonto(montoTotal);  // Establecer el monto calculado
        pago.setNumTarjeta(pagoDTO.getNumTarjeta());
        pago.setNombreTitular(pagoDTO.getNombreTitular());
        pago.setVencimiento(pagoDTO.getVencimiento());
        pago.setCodSeguridad(pagoDTO.getCodSeguridad());
        pago.setMetodoPago(pagoDTO.getMetodoPago());
        pago.setEstadoPago(EstadoPago.PAGADO); // Cambiar el estado a "Pagado"
        turno.setPago(pago);


        // Guardar el pago en la base de datos
        Pago pagoGuardado = pagoRepository.save(pago);

        // Actualizar el estado del turno a "Pagado" (o un estado personalizado)
        turno.setEstado(EstadoTurno.PAGADO);
        turnoRepository.save(turno);

        return pagoGuardado;
    }



}

