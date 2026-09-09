package com.club.socios.service;

import com.club.socios.domain.Socio;
import com.club.socios.dto.SocioDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Contrato de la capa de SERVICIO para Socio (patrón MVC: el Controller
 * nunca accede al Repository directamente, siempre pasa por el Service,
 * que es donde vive la lógica de negocio y las transacciones).
 */
public interface SocioService {

    List<SocioDTO> listarActivos();

    List<SocioDTO> buscar(String texto);

    SocioDTO obtenerPorId(Long id);

    /** Devuelve la entidad de dominio (uso interno entre servicios, ej. RegistroAccesoService). */
    Socio obtenerEntidadPorId(Long id);

    /**
     * Alta de un socio. Internamente crea también, en la misma transacción,
     * su {@code GrupoFamiliar} (composición Socio-GrupoFamiliar) para que
     * desde el alta ya pueda empezar a cargarse el grupo familiar.
     */
    SocioDTO crear(SocioDTO dto, MultipartFile fotoRostro);

    SocioDTO actualizar(Long id, SocioDTO dto, MultipartFile fotoRostro);

    /** Baja lógica (soft delete): no se borra físicamente para preservar el historial. */
    void eliminar(Long id);
}
