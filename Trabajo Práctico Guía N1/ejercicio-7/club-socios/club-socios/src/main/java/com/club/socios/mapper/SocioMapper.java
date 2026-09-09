package com.club.socios.mapper;

import com.club.socios.domain.Socio;
import com.club.socios.dto.SocioDTO;
import org.springframework.stereotype.Component;

/**
 * ============================================================================
 * MAPPER Entidad <-> DTO: Socio
 * ============================================================================
 * Traduce entre la entidad JPA (que vive dentro de la sesión de persistencia,
 * con proxies y colecciones lazy) y el DTO plano que consumen el Controller
 * y la vista Thymeleaf. Se implementa a mano (en vez de generarlo con una
 * librería) para que el flujo de datos entre capas quede 100% explícito.
 * ============================================================================
 */
@Component
public class SocioMapper {

    public SocioDTO toDTO(Socio socio) {
        if (socio == null) {
            return null;
        }
        return SocioDTO.builder()
                .id(socio.getId())
                .nombre(socio.getNombre())
                .apellido(socio.getApellido())
                .dni(socio.getDni())
                .fechaNacimiento(socio.getFechaNacimiento())
                .email(socio.getEmail())
                .telefono(socio.getTelefono())
                .numeroSocio(socio.getNumeroSocio())
                .fechaAlta(socio.getFechaAlta())
                .categoria(socio.getCategoria())
                .activo(socio.isActivo())
                .urlImagenRostro(socio.getImagenRostro() != null ? socio.getImagenRostro().getUrlArchivo() : null)
                .cantidadFamiliares(socio.getGrupoFamiliar() != null
                        ? socio.getGrupoFamiliar().getFamiliares().size()
                        : 0)
                .build();
    }

    /** Copia los datos editables del DTO hacia una entidad (nueva o existente). */
    public void copyToEntity(SocioDTO dto, Socio socio) {
        socio.setNombre(dto.getNombre());
        socio.setApellido(dto.getApellido());
        socio.setDni(dto.getDni());
        socio.setFechaNacimiento(dto.getFechaNacimiento());
        socio.setEmail(dto.getEmail());
        socio.setTelefono(dto.getTelefono());
        socio.setNumeroSocio(dto.getNumeroSocio());
        socio.setCategoria(dto.getCategoria());
        if (dto.getFechaAlta() != null) {
            socio.setFechaAlta(dto.getFechaAlta());
        }
    }
}
