package com.adri.api_spa.services;

import com.adri.api_spa.models.Empleo;
import com.adri.api_spa.models.Postulacion;
import com.adri.api_spa.repositories.IEmpleoRepository;
import com.adri.api_spa.repositories.IPostulacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class PostulacionService {

    @Autowired
    private IPostulacionRepository postulacionRepository;

    @Autowired
    private IEmpleoRepository empleoRepository;


    public Postulacion savePostulacionWithCv(Long empleoId, MultipartFile cvFile) throws IOException {
        Empleo empleo = empleoRepository.findById(empleoId)
                .orElseThrow(() -> new RuntimeException("Empleo no encontrado"));

        byte[] fileBytes = cvFile.getBytes();
        String fileName = cvFile.getOriginalFilename();

        Postulacion postulacion = new Postulacion();
        postulacion.setEmpleo(empleo);
        postulacion.setCv(fileBytes);
        postulacion.setCvFileName(fileName);

        return postulacionRepository.save(postulacion);
    }





}