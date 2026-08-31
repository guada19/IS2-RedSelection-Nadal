package com.colegio.spark.mapper;

import com.colegio.spark.dto.response.NotaResponseDTO;
import com.colegio.spark.model.Nota;
import org.springframework.stereotype.Component;

/** Mapper Entidad <-> DTO para Nota. */
@Component
public class NotaMapper {

    public NotaResponseDTO toResponseDTO(Nota nota) {
        if (nota == null) {
            return null;
        }
        return NotaResponseDTO.builder()
                .id(nota.getId())
                .alumnoId(nota.getAlumno().getId())
                .alumnoNombreCompleto(nota.getAlumno().getNombre() + " " + nota.getAlumno().getApellido())
                .materiaId(nota.getMateria().getId())
                .materiaNombre(nota.getMateria().getNombre())
                .docenteNombreCompleto(nota.getDocente().getNombre() + " " + nota.getDocente().getApellido())
                .valor(nota.getValor())
                .periodo(nota.getPeriodo())
                .fechaEvaluacion(nota.getFechaEvaluacion())
                .observaciones(nota.getObservaciones())
                .build();
    }

}
