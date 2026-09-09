package com.club.socios.service.impl;

import com.club.socios.domain.GrupoFamiliar;
import com.club.socios.domain.ImagenRostro;
import com.club.socios.domain.Socio;
import com.club.socios.dto.SocioDTO;
import com.club.socios.exception.ReglaDeNegocioException;
import com.club.socios.exception.RecursoNoEncontradoException;
import com.club.socios.mapper.SocioMapper;
import com.club.socios.repository.GrupoFamiliarRepository;
import com.club.socios.repository.SocioRepository;
import com.club.socios.service.ImagenRostroService;
import com.club.socios.service.SocioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

/**
 * ============================================================================
 * SERVICE: Socio (implementación)
 * ============================================================================
 * Concentra la lógica de negocio del alta/edición/baja de socios. Trabaja
 * siempre con DTOs hacia "afuera" (Controller) y con Entidades hacia
 * "adentro" (Repository/ORM), delegando la traducción al {@link SocioMapper}.
 *
 * @Transactional asegura que, por ejemplo, crear el Socio + su ImagenRostro
 * + su GrupoFamiliar (3 inserts relacionados) ocurra de forma atómica: si
 * algo falla, se revierte todo (ACID a nivel de la capa de servicio).
 * ============================================================================
 */
@Service
@RequiredArgsConstructor
public class SocioServiceImpl implements SocioService {

    private final SocioRepository socioRepository;
    private final GrupoFamiliarRepository grupoFamiliarRepository;
    private final SocioMapper socioMapper;
    private final ImagenRostroService imagenRostroService;

    @Override
    @Transactional(readOnly = true)
    public List<SocioDTO> listarActivos() {
        return socioRepository.findByActivoTrue().stream()
                .map(socioMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SocioDTO> buscar(String texto) {
        if (texto == null || texto.isBlank()) {
            return listarActivos();
        }
        return socioRepository.buscarPorTexto(texto).stream()
                .map(socioMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SocioDTO obtenerPorId(Long id) {
        return socioMapper.toDTO(obtenerEntidadPorId(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Socio obtenerEntidadPorId(Long id) {
        return socioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un socio con id " + id));
    }

    @Override
    @Transactional
    public SocioDTO crear(SocioDTO dto, MultipartFile fotoRostro) {
        socioRepository.findByDni(dto.getDni()).ifPresent(s -> {
            throw new ReglaDeNegocioException("Ya existe un socio registrado con el DNI " + dto.getDni());
        });

        Socio socio = new Socio();
        socioMapper.copyToEntity(dto, socio);
        socio.setFechaAlta(dto.getFechaAlta() != null ? dto.getFechaAlta() : LocalDate.now());
        socio.setActivo(true);

        // Composición Persona-ImagenRostro
        if (fotoRostro != null && !fotoRostro.isEmpty()) {
            ImagenRostro imagen = imagenRostroService.procesarImagen(socio, fotoRostro);
            socio.setImagenRostro(imagen);
        }

        Socio socioGuardado = socioRepository.save(socio);

        // Composición Socio-GrupoFamiliar: se crea automáticamente al dar de
        // alta al titular, cumpliendo la funcionalidad pedida en el punto "a".
        GrupoFamiliar grupo = new GrupoFamiliar();
        grupo.setSocioTitular(socioGuardado);
        grupo.setNombreGrupo("Familia " + socioGuardado.getApellido());
        grupoFamiliarRepository.save(grupo);
        socioGuardado.setGrupoFamiliar(grupo);

        return socioMapper.toDTO(socioGuardado);
    }

    @Override
    @Transactional
    public SocioDTO actualizar(Long id, SocioDTO dto, MultipartFile fotoRostro) {
        Socio socio = obtenerEntidadPorId(id);

        socioRepository.findByDni(dto.getDni())
                .filter(otro -> !otro.getId().equals(id))
                .ifPresent(otro -> {
                    throw new ReglaDeNegocioException("Ya existe otro socio con el DNI " + dto.getDni());
                });

        socioMapper.copyToEntity(dto, socio);

        if (fotoRostro != null && !fotoRostro.isEmpty()) {
            ImagenRostro imagen = imagenRostroService.procesarImagen(socio, fotoRostro);
            socio.setImagenRostro(imagen);
        }

        return socioMapper.toDTO(socioRepository.save(socio));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Socio socio = obtenerEntidadPorId(id);
        // Baja lógica: se conserva el historial de accesos y pagos.
        socio.setActivo(false);
        socioRepository.save(socio);
    }
}
