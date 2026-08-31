package com.colegio.spark.mapper;

import com.colegio.spark.dto.response.AlumnoResponseDTO;
import com.colegio.spark.model.Alumno;
import org.springframework.stereotype.Component;

/**
 * Mapper Entidad <-> DTO para Alumno.
 * (Al igual que con Aula, la construccion de la entidad a partir del DTO de
 * entrada se resuelve en AlumnoServiceImpl, ya que requiere resolver Grado y
 * Aula desde sus respectivos repositorios).
 */
@Component
public class AlumnoMapper {

    public AlumnoResponseDTO toResponseDTO(Alumno alumno) {
        if (alumno == null) {
            return null;
        }
        return AlumnoResponseDTO.builder()
                .id(alumno.getId())
                .nombre(alumno.getNombre())
                .apellido(alumno.getApellido())
                .dni(alumno.getDni())
                .fechaNacimiento(alumno.getFechaNacimiento())
                .gradoId(alumno.getGrado() != null ? alumno.getGrado().getId() : null)
                .gradoNombre(alumno.getGrado() != null ? alumno.getGrado().getNombre() : null)
                .aulaId(alumno.getAula() != null ? alumno.getAula().getId() : null)
                .aulaNombre(alumno.getAula() != null ? alumno.getAula().getNombre() : null)
                .build();
    }

}
