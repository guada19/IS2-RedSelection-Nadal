package com.club.socios.service;

import com.club.socios.domain.ImagenRostro;
import com.club.socios.domain.Persona;
import org.springframework.web.multipart.MultipartFile;

/**
 * Servicio dedicado a la gestión de la foto de rostro (composición con
 * Persona). Se separa en su propia interfaz porque el almacenamiento físico
 * del archivo es una responsabilidad distinta a la de "administrar socios":
 * principio de responsabilidad única (SRP).
 */
public interface ImagenRostroService {

    /**
     * Guarda el archivo recibido por formulario en el disco/almacenamiento
     * configurado (ver {@code app.imagenes.directorio}) y arma la entidad
     * {@link ImagenRostro} lista para asociarse (composición) a la persona.
     */
    ImagenRostro procesarImagen(Persona persona, MultipartFile archivo);
}
