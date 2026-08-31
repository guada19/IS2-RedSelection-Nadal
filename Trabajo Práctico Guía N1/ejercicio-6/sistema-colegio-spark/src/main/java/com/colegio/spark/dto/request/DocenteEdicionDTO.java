package com.colegio.spark.dto.request;

import com.colegio.spark.model.enums.Sexo;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

/**
 * DTO de entrada para que un ADMINISTRADOR edite los datos personales de un
 * docente ya existente (nombre, apellido, sexo, fecha de nacimiento).
 * A diferencia de DocenteRegistroDTO, este DTO NO incluye email ni password:
 * el cambio de contraseña tiene su propio caso de uso y DTO
 * (ver CambioPasswordDTO), y el email/username no se permite modificar desde
 * esta pantalla para no romper la trazabilidad de auditoria (creadoPor).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocenteEdicionDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 80)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 80)
    private String apellido;

    @NotNull(message = "Debe seleccionar el sexo")
    private Sexo sexo;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser anterior a hoy")
    private LocalDate fechaNacimiento;

}
