package com.colegio.spark.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * DTO de entrada para asignar un docente a una materia dentro de un aula
 * (quien dicta que, y donde). Referencia las tres entidades relacionadas por id.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AsignacionDocenteDTO {

    @NotNull(message = "Debe seleccionar el docente")
    private Long docenteId;

    @NotNull(message = "Debe seleccionar la materia")
    private Long materiaId;

    @NotNull(message = "Debe seleccionar el aula")
    private Long aulaId;

}
