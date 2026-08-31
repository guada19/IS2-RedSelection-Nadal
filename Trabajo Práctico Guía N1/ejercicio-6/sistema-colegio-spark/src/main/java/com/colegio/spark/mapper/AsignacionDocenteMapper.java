package com.colegio.spark.mapper;

import com.colegio.spark.dto.response.AsignacionDocenteResponseDTO;
import com.colegio.spark.model.AsignacionDocente;
import org.springframework.stereotype.Component;

/** Mapper Entidad <-> DTO para AsignacionDocente. */
@Component
public class AsignacionDocenteMapper {

    public AsignacionDocenteResponseDTO toResponseDTO(AsignacionDocente asignacion) {
        if (asignacion == null) {
            return null;
        }
        return AsignacionDocenteResponseDTO.builder()
                .id(asignacion.getId())
                .docenteId(asignacion.getDocente().getId())
                .docenteNombreCompleto(asignacion.getDocente().getNombre() + " " + asignacion.getDocente().getApellido())
                .materiaId(asignacion.getMateria().getId())
                .materiaNombre(asignacion.getMateria().getNombre())
                .aulaId(asignacion.getAula().getId())
                .aulaNombre(asignacion.getAula().getNombre())
                .build();
    }

}
