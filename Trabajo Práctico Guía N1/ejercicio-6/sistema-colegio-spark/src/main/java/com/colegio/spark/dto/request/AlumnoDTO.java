package com.colegio.spark.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

/** DTO de entrada para alta/edicion de un Alumno. Referencia Grado y Aula por id. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlumnoDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 80)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 80)
    private String apellido;

    @NotBlank(message = "El DNI es obligatorio")
    @Size(max = 15)
    private String dni;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser anterior a hoy")
    private LocalDate fechaNacimiento;

    @NotNull(message = "Debe seleccionar el grado del alumno")
    private Long gradoId;

    @NotNull(message = "Debe seleccionar el aula del alumno")
    private Long aulaId;

}
