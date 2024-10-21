package com.adri.api_spa.controllers;

import com.adri.api_spa.Utils.ApiError;
import com.adri.api_spa.Utils.ResponseHandler;
import com.adri.api_spa.dtos.DtoPago;
import com.adri.api_spa.models.Cliente;
import com.adri.api_spa.models.Pago;
import com.adri.api_spa.models.Turno;
import com.adri.api_spa.models.Usuarios;
import com.adri.api_spa.repositories.IClienteRepository;
import com.adri.api_spa.repositories.IPagoRepository;
import com.adri.api_spa.repositories.ITurnoRepository;
import com.adri.api_spa.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
    IClienteRepository clienteRepository;

    @Autowired
    IPagoRepository pagoRepository;

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
    public ResponseEntity<?> procesarPago(@Validated @RequestBody DtoPago pagoDTO, Errors errors) {
        try {


            if (errors.hasErrors()){

                return ResponseHandler.generateResponse("Complete los datos correctamente",HttpStatus.BAD_REQUEST,new ApiError(errors).getErrores());

            }

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




    //// LISTADOS
   // Listado de pagos realizados por un cliente especifico (Secretaria: Acceso a los pagos que se realizan.)


    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<?> listadoPagosPorCliente(@PathVariable Long idCliente) {

        Cliente cliente = clienteRepository.findById(idCliente).isPresent() ? clienteRepository.findById(idCliente).get()  : null ;

        List<Pago> listadoPagos = pagoRepository.findPagosByClienteId(idCliente);



        return ResponseHandler.generateResponse("Listado de Pagos realizados por el cliente "+ cliente.getUsuario().getNombre()+ " "
                + cliente.getUsuario().getApellido(), HttpStatus.OK, listadoPagos);

    }




    ///// Listado de los ingresos recibidos en total,
    // en un rango de fecha especifico
    // (Informe de Ingresos en un rango de fecha a ingresar por teclado discriminado por tipo de pago.)



    @GetMapping("/ingresos")
    public ResponseEntity<?> obtenerInformeDeIngresos(
            @RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam("fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        List<Object[]> ingresos = pagoService.obtenerIngresosPorTipoDePagoEnRangoDeFechas(fechaInicio, fechaFin);

        if (ingresos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron ingresos en el rango de fechas dado.");
        }

        return ResponseEntity.ok(ingresos);
    }








}
