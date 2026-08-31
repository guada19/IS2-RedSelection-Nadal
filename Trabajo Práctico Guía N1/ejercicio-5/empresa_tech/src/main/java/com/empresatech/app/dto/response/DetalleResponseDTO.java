package com.empresatech.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * DTO de salida para una línea de Detalle dentro de una factura.
 *
 * No incluye "facturaId" ni un FacturaResponseDTO: al igual que
 * StockResponseDTO, está pensado para viajar siempre ANIDADO dentro de
 * un FacturaClienteResponseDTO/FacturaProveedorResponseDTO, nunca de
 * forma independiente. Esto es lo que evita el ciclo
 * Factura -> Detalles -> Factura -> ... que existe a nivel de entidades
 * (Factura tiene una lista de Detalle, y cada Detalle referencia de
 * vuelta a su Factura).
 *
 * Sí incluye "productoNombre" y "precioUnitario" (aplanados desde la
 * entidad Producto) porque son justamente los datos que una vista de
 * "detalle de factura" necesita mostrar en cada renglón, sin tener que
 * exponer el Producto completo.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleResponseDTO {

    private Long id;
    private String productoNombre;
    private BigDecimal precioUnitario;
    private Integer cantidad;
    private BigDecimal subtotal;
}
