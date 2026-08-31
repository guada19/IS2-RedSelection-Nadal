package com.colegio.spark.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/** DTO de entrada para alta/edicion de un Grado. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradoDTO {

    @NotBlank(message = "El nombre del grado es obligatorio")
    @Size(max = 60)
    private String nombre;

    @NotBlank(message = "El nivel es obligatorio")
    @Size(max = 40)
    private String nivel;

}
