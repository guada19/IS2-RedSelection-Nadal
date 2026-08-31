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
 * DTO de entrada para registrar una venta (Factura a un Cliente).
 *
 * No incluye "nroFactura", "fecha", "estado" ni "total": son datos que
 * genera el propio sistema al procesar la venta (número correlativo,
 * fecha del servidor, estado inicial PENDIENTE, total calculado sumando
 * los subtotales de cada detalle), no datos que el usuario deba
 * completar manualmente. Esto es intencional: cuanto menos se le pida
 * "confiar" al cliente/usuario para calcular valores derivados, menor es
 * la superficie de error o manipulación indebida.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacturaClienteRequestDTO {

    @NotNull(message = "El cliente es obligatorio")
    private Long idCliente;

    @NotEmpty(message = "La factura debe tener al menos un detalle")
    // @NotEmpty: además de exigir que la lista no sea null, exige que
    // tenga al menos un elemento. Una factura sin líneas no representa
    // ninguna venta real.
    @Valid
    // @Valid: le indica al motor de validación que descienda dentro de
    // cada elemento de la lista y también aplique las anotaciones
    // definidas en DetalleRequestDTO (idProducto/cantidad obligatorios,
    // cantidad positiva). Sin @Valid aquí, un DetalleRequestDTO inválido
    // dentro de la lista pasaría desapercibido.
    private List<DetalleRequestDTO> detalles;
}
