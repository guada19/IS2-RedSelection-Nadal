package com.club.socios.mapper;

import com.club.socios.domain.GrupoFamiliar;
import com.club.socios.dto.GrupoFamiliarDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GrupoFamiliarMapper {

    private final FamiliarSocioMapper familiarSocioMapper;

    public GrupoFamiliarMapper(FamiliarSocioMapper familiarSocioMapper) {
        this.familiarSocioMapper = familiarSocioMapper;
    }

    public GrupoFamiliarDTO toDTO(GrupoFamiliar grupo) {
        if (grupo == null) {
            return null;
        }
        List<com.club.socios.dto.FamiliarSocioDTO> familiares = grupo.getFamiliares().stream()
                .map(familiarSocioMapper::toDTO)
                .collect(Collectors.toList());

        return GrupoFamiliarDTO.builder()
                .id(grupo.getId())
                .nombreGrupo(grupo.getNombreGrupo())
                .socioTitularId(grupo.getSocioTitular() != null ? grupo.getSocioTitular().getId() : null)
                .socioTitularNombreCompleto(grupo.getSocioTitular() != null ? grupo.getSocioTitular().getNombreCompleto() : null)
                .familiares(familiares)
                .cantidadIntegrantes(grupo.getCantidadIntegrantes())
                .build();
    }
}
