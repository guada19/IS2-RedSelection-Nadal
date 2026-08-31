package com.colegio.spark.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** DTO de salida con los datos de un Grado para las vistas. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradoResponseDTO {

    private Long id;
    private String nombre;
    private String nivel;

}
