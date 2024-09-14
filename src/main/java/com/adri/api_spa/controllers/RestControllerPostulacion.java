package com.adri.api_spa.controllers;


import com.adri.api_spa.Utils.ResponseHandler;
import com.adri.api_spa.models.EstadoEmpleo;
import com.adri.api_spa.models.Postulacion;
import com.adri.api_spa.models.Servicio;
import com.adri.api_spa.repositories.IPostulacionRepository;
import com.adri.api_spa.services.PostulacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/postulacion")
public class RestControllerPostulacion {

    @Autowired
    private PostulacionService postulacionService;

    @Autowired
    private IPostulacionRepository postulacionRepository;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadCv(
            @RequestParam("id_empleo") Long empleoId,
            @RequestParam("cv") MultipartFile cvFile) throws IOException {

        try {
            Postulacion postulacion = postulacionService.savePostulacionWithCv(empleoId, cvFile);
            return ResponseEntity.ok("Archivo subido exitosamente: " + postulacion.getIdPostulacion());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al subir el archivo.");
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadCv(@PathVariable Long id) {
        Postulacion postulacion = postulacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Postulación no encontrada"));

        byte[] fileBytes = postulacion.getCv();
        String fileName = postulacion.getCvFileName(); // Obtén el nombre del archivo

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition
                .builder("inline")
                .filename(fileName)
                .build()); // Usa el nombre del archivo

        return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
    }



}