package com.club.socios.service.impl;

import com.club.socios.domain.ImagenRostro;
import com.club.socios.domain.Persona;
import com.club.socios.service.ImagenRostroService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class ImagenRostroServiceImpl implements ImagenRostroService {

    @Value("${app.imagenes.directorio:./imagenes-rostros}")
    private String directorioImagenes;

    @Override
    public ImagenRostro procesarImagen(Persona persona, MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            return null;
        }
        try {
            Path directorio = Paths.get(directorioImagenes);
            Files.createDirectories(directorio);

            String extension = StringUtils.getFilenameExtension(archivo.getOriginalFilename());
            String nombreArchivo = "persona_" + persona.getDni() + "_" + UUID.randomUUID()
                    + (extension != null ? "." + extension : "");
            Path destino = directorio.resolve(nombreArchivo);
            Files.copy(archivo.getInputStream(), destino);

            ImagenRostro imagen = persona.getImagenRostro() != null ? persona.getImagenRostro() : new ImagenRostro();
            imagen.setPersona(persona);
            imagen.setUrlArchivo(destino.toString());
            imagen.setFechaCaptura(LocalDateTime.now());
            // El "hash biométrico" representa, de forma simulada, el vector facial
            // que en un sistema real generaría un motor de reconocimiento facial
            // (ej. OpenCV / AWS Rekognition / Azure Face). Se deja simulado porque
            // integrar un motor real excede el alcance de este ejercicio académico.
            imagen.setHashBiometrico(UUID.randomUUID().toString());
            return imagen;
        } catch (IOException e) {
            log.error("Error al guardar la imagen de rostro de la persona {}", persona.getDni(), e);
            throw new UncheckedIOException("No se pudo guardar la imagen de rostro", e);
        }
    }
}
