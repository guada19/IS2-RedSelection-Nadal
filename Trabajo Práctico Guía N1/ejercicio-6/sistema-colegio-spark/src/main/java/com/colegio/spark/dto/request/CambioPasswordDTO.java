package com.colegio.spark.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de entrada para el caso de uso "cambiar mi contraseña" (docente
 * autenticado). Requiere la contraseña actual (para verificar identidad,
 * ver DocenteServiceImpl.cambiarPassword) ademas de la nueva contraseña y su
 * confirmacion.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CambioPasswordDTO {

    @NotBlank(message = "Debe ingresar su contraseña actual")
    private String passwordActual;

    @NotBlank(message = "La nueva contraseña es obligatoria")
    @Size(min = 8, max = 100, message = "La nueva contraseña debe tener entre 8 y 100 caracteres")
    private String passwordNueva;

    @NotBlank(message = "Debe confirmar la nueva contraseña")
    private String confirmarPasswordNueva;

}
