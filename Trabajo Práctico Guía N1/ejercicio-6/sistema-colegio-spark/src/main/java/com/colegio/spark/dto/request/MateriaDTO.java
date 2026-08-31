package com.colegio.spark.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/** DTO de entrada para alta/edicion de una Materia. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MateriaDTO {

    @NotBlank(message = "El nombre de la materia es obligatorio")
    @Size(max = 80)
    private String nombre;

    @Size(max = 255)
    private String descripcion;

}
