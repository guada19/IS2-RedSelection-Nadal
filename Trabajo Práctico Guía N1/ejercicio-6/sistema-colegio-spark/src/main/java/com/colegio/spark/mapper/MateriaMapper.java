package com.colegio.spark.mapper;

import com.colegio.spark.dto.request.MateriaDTO;
import com.colegio.spark.dto.response.MateriaResponseDTO;
import com.colegio.spark.model.Materia;
import org.springframework.stereotype.Component;

/** Mapper Entidad <-> DTO para Materia. */
@Component
public class MateriaMapper {

    public MateriaResponseDTO toResponseDTO(Materia materia) {
        if (materia == null) {
            return null;
        }
        return MateriaResponseDTO.builder()
                .id(materia.getId())
                .nombre(materia.getNombre())
                .descripcion(materia.getDescripcion())
                .build();
    }

    public Materia toEntity(MateriaDTO dto) {
        return Materia.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .build();
    }

    public void actualizarEntity(Materia materia, MateriaDTO dto) {
        materia.setNombre(dto.getNombre());
        materia.setDescripcion(dto.getDescripcion());
    }

}
