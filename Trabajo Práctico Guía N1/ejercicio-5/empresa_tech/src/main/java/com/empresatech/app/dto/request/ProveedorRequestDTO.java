package com.empresatech.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de entrada para crear/actualizar un Proveedor. Ver ClienteRequestDTO
 * para la explicación general del patrón Request/Response.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProveedorRequestDTO {

    @NotBlank(message = "El CUIT es obligatorio")
    @Pattern(regexp = "\\d{2}-?\\d{8}-?\\d{1}", message = "El CUIT debe tener el formato NN-NNNNNNNN-N")
    // Formato típico de CUIT argentino (con o sin guiones): 2 dígitos,
    // 8 dígitos y 1 dígito verificador.
    private String cuit;

    @NotBlank(message = "La razón social es obligatoria")
    @Size(max = 150, message = "La razón social no puede superar los 150 caracteres")
    private String razonSocial;

    @Pattern(regexp = "^$|\\+?[0-9\\-\\s]{6,30}", message = "El teléfono tiene un formato inválido")
    private String telefono;

    // "activo" no se incluye: por defecto todo proveedor nuevo se crea
    // activo=true (se define en la capa de servicio), y darlo de baja es
    // una operación explícita, no un campo libre del formulario de alta.
}
