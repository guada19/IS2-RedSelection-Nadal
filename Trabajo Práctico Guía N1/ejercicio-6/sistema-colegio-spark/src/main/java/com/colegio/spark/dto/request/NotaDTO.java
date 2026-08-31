package com.colegio.spark.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO de entrada para que un docente cargue/edite la nota de un alumno en
 * una materia. El docente que carga la nota NO viaja en el DTO: se toma del
 * usuario autenticado en el Controller (principio de "nunca confiar en el
 * cliente" para datos sensibles de identidad/seguridad), evitando que un
 * docente pudiera adjudicarle una carga a otro colega.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotaDTO {

    @NotNull(message = "Debe seleccionar el alumno")
    private Long alumnoId;

    @NotNull(message = "Debe seleccionar la materia")
    private Long materiaId;

    @NotNull(message = "La calificacion es obligatoria")
    @DecimalMin(value = "1.00", message = "La nota minima es 1.00")
    @DecimalMax(value = "10.00", message = "La nota maxima es 10.00")
    private BigDecimal valor;

    @NotBlank(message = "Debe indicar el periodo (ej: 1er Trimestre)")
    @Size(max = 40)
    private String periodo;

    @NotNull(message = "La fecha de evaluacion es obligatoria")
    private LocalDate fechaEvaluacion;

    @Size(max = 255)
    private String observaciones;

}
