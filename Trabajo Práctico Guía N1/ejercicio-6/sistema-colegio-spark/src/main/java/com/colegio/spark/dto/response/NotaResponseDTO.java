package com.colegio.spark.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/** DTO de salida con los datos (aplanados) de una nota, listos para la vista. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotaResponseDTO {

    private Long id;
    private Long alumnoId;
    private String alumnoNombreCompleto;
    private Long materiaId;
    private String materiaNombre;
    private String docenteNombreCompleto;
    private BigDecimal valor;
    private String periodo;
    private LocalDate fechaEvaluacion;
    private String observaciones;

}
