package com.colegio.spark.mapper;

import com.colegio.spark.dto.request.GradoDTO;
import com.colegio.spark.dto.response.GradoResponseDTO;
import com.colegio.spark.model.Grado;
import org.springframework.stereotype.Component;

/** Mapper Entidad <-> DTO para Grado. */
@Component
public class GradoMapper {

    public GradoResponseDTO toResponseDTO(Grado grado) {
        if (grado == null) {
            return null;
        }
        return GradoResponseDTO.builder()
                .id(grado.getId())
                .nombre(grado.getNombre())
                .nivel(grado.getNivel())
                .build();
    }

    /** Crea una nueva entidad Grado (sin id, sin auditoria) a partir del DTO de entrada. */
    public Grado toEntity(GradoDTO dto) {
        return Grado.builder()
                .nombre(dto.getNombre())
                .nivel(dto.getNivel())
                .build();
    }

    /** Aplica los cambios del DTO sobre una entidad Grado ya existente (edicion). */
    public void actualizarEntity(Grado grado, GradoDTO dto) {
        grado.setNombre(dto.getNombre());
        grado.setNivel(dto.getNivel());
    }

}
