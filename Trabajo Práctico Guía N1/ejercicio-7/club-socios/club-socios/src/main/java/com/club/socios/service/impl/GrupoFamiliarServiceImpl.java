package com.club.socios.service.impl;

import com.club.socios.domain.FamiliarSocio;
import com.club.socios.domain.GrupoFamiliar;
import com.club.socios.domain.ImagenRostro;
import com.club.socios.dto.FamiliarSocioDTO;
import com.club.socios.dto.GrupoFamiliarDTO;
import com.club.socios.exception.RecursoNoEncontradoException;
import com.club.socios.mapper.FamiliarSocioMapper;
import com.club.socios.mapper.GrupoFamiliarMapper;
import com.club.socios.repository.FamiliarSocioRepository;
import com.club.socios.repository.GrupoFamiliarRepository;
import com.club.socios.service.GrupoFamiliarService;
import com.club.socios.service.ImagenRostroService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * ============================================================================
 * SERVICE: GrupoFamiliar (implementación)
 * ============================================================================
 * Administra la AGREGACIÓN GrupoFamiliar (1) ---- (*) FamiliarSocio: agregar
 * o quitar un integrante NO afecta la existencia de la Persona (a diferencia
 * de la composición Socio-GrupoFamiliar, que sí es dueña exclusiva del ciclo
 * de vida de su "parte").
 * ============================================================================
 */
@Service
@RequiredArgsConstructor
public class GrupoFamiliarServiceImpl implements GrupoFamiliarService {

    private final GrupoFamiliarRepository grupoFamiliarRepository;
    private final FamiliarSocioRepository familiarSocioRepository;
    private final GrupoFamiliarMapper grupoFamiliarMapper;
    private final FamiliarSocioMapper familiarSocioMapper;
    private final ImagenRostroService imagenRostroService;

    @Override
    @Transactional(readOnly = true)
    public GrupoFamiliarDTO obtenerPorSocio(Long socioId) {
        return grupoFamiliarMapper.toDTO(obtenerEntidadPorSocio(socioId));
    }

    @Override
    @Transactional(readOnly = true)
    public GrupoFamiliarDTO obtenerPorId(Long grupoFamiliarId) {
        GrupoFamiliar grupo = grupoFamiliarRepository.findById(grupoFamiliarId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Grupo familiar no encontrado: " + grupoFamiliarId));
        return grupoFamiliarMapper.toDTO(grupo);
    }

    @Override
    @Transactional(readOnly = true)
    public GrupoFamiliar obtenerEntidadPorSocio(Long socioId) {
        return grupoFamiliarRepository.findBySocioTitularId(socioId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "El socio " + socioId + " no tiene un grupo familiar asociado"));
    }

    @Override
    @Transactional
    public FamiliarSocioDTO agregarFamiliar(Long socioId, FamiliarSocioDTO dto, MultipartFile fotoRostro) {
        GrupoFamiliar grupo = obtenerEntidadPorSocio(socioId);

        FamiliarSocio familiar = new FamiliarSocio();
        familiarSocioMapper.copyToEntity(dto, familiar);
        familiar.setActivo(true);

        // Composición Persona-ImagenRostro también aplica a los familiares:
        // el sistema debe reconocer el rostro de TODO el grupo familiar.
        if (fotoRostro != null && !fotoRostro.isEmpty()) {
            ImagenRostro imagen = imagenRostroService.procesarImagen(familiar, fotoRostro);
            familiar.setImagenRostro(imagen);
        }

        // agregarFamiliar() setea ambos lados de la relación (agregación bidireccional)
        grupo.agregarFamiliar(familiar);
        FamiliarSocio guardado = familiarSocioRepository.save(familiar);

        return familiarSocioMapper.toDTO(guardado);
    }

    @Override
    @Transactional
    public void quitarFamiliar(Long grupoFamiliarId, Long familiarId) {
        GrupoFamiliar grupo = grupoFamiliarRepository.findById(grupoFamiliarId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Grupo familiar no encontrado: " + grupoFamiliarId));

        FamiliarSocio familiar = familiarSocioRepository.findById(familiarId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Familiar no encontrado: " + familiarId));

        // Al ser AGREGACIÓN, sólo se rompe el vínculo: el familiar sigue
        // existiendo como Persona (con su historial de accesos intacto).
        grupo.quitarFamiliar(familiar);
        familiarSocioRepository.save(familiar);
    }
}
