package com.club.socios.mapper;

import com.club.socios.domain.FamiliarSocio;
import com.club.socios.dto.FamiliarSocioDTO;
import org.springframework.stereotype.Component;

@Component
public class FamiliarSocioMapper {

    public FamiliarSocioDTO toDTO(FamiliarSocio familiar) {
        if (familiar == null) {
            return null;
        }
        return FamiliarSocioDTO.builder()
                .id(familiar.getId())
                .nombre(familiar.getNombre())
                .apellido(familiar.getApellido())
                .dni(familiar.getDni())
                .fechaNacimiento(familiar.getFechaNacimiento())
                .parentesco(familiar.getParentesco())
                .grupoFamiliarId(familiar.getGrupoFamiliar() != null ? familiar.getGrupoFamiliar().getId() : null)
                .urlImagenRostro(familiar.getImagenRostro() != null ? familiar.getImagenRostro().getUrlArchivo() : null)
                .build();
    }

    public void copyToEntity(FamiliarSocioDTO dto, FamiliarSocio familiar) {
        familiar.setNombre(dto.getNombre());
        familiar.setApellido(dto.getApellido());
        familiar.setDni(dto.getDni());
        familiar.setFechaNacimiento(dto.getFechaNacimiento());
        familiar.setParentesco(dto.getParentesco());
    }
}
