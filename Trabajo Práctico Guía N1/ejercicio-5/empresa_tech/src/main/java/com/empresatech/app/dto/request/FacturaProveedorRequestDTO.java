package com.empresatech.app.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * DTO de entrada para registrar una compra (Factura a un Proveedor).
 * Estructuralmente idéntico a FacturaClienteRequestDTO salvo por
 * "idProveedor" en lugar de "idCliente"; se mantiene como una clase
 * separada (en vez de generalizar con un campo "idEntidad" genérico)
 * porque representa un concepto de negocio distinto (una compra, no una
 * venta) y porque el controlador que lo recibe también es distinto
 * (típicamente POST /facturas-proveedor vs. POST /facturas-cliente).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacturaProveedorRequestDTO {

    @NotNull(message = "El proveedor es obligatorio")
    private Long idProveedor;

    @NotEmpty(message = "La factura debe tener al menos un detalle")
    @Valid
    private List<DetalleRequestDTO> detalles;
}
