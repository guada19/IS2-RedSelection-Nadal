package com.empresatech.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de salida para mostrar un Proveedor. Ver ClienteResponseDTO para la
 * explicación general del propósito de los Response DTOs.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProveedorResponseDTO {

    private Long id;
    private String cuit;
    private String razonSocial;
    private String telefono;
    private boolean activo;
}
