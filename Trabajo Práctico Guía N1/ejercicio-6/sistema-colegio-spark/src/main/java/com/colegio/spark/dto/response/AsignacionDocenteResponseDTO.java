package com.colegio.spark.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** DTO de salida con los datos (aplanados) de una asignacion docente-materia-aula. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AsignacionDocenteResponseDTO {

    private Long id;
    private Long docenteId;
    private String docenteNombreCompleto;
    private Long materiaId;
    private String materiaNombre;
    private Long aulaId;
    private String aulaNombre;

}
