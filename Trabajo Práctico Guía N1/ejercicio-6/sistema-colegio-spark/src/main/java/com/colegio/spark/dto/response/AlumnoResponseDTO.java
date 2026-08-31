package com.colegio.spark.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * DTO de salida con los datos de un Alumno, incluyendo (aplanados) el nombre
 * de su Grado y de su Aula, listos para mostrar en las tablas Thymeleaf sin
 * navegar relaciones LAZY desde la vista.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlumnoResponseDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private String dni;
    private LocalDate fechaNacimiento;
    private Long gradoId;
    private String gradoNombre;
    private Long aulaId;
    private String aulaNombre;

}
