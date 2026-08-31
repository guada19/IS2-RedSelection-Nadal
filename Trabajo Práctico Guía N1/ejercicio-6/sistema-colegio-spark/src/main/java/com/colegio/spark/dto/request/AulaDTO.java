package com.colegio.spark.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

/** DTO de entrada para alta/edicion de un Aula. Referencia al Grado por id. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AulaDTO {

    @NotBlank(message = "El nombre del aula es obligatorio")
    @Size(max = 60)
    private String nombre;

    @NotNull(message = "La capacidad es obligatoria")
    @Min(value = 1, message = "La capacidad debe ser mayor a 0")
    private Integer capacidad;

    @NotNull(message = "Debe seleccionar el grado al que pertenece el aula")
    private Long gradoId;

}
