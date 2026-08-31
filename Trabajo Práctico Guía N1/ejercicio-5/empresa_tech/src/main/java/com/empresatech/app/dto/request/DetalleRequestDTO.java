package com.empresatech.app.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de entrada para una línea de detalle dentro de una factura
 * (de cliente o de proveedor).
 *
 * Es deliberadamente minimalista: el usuario que carga una venta/compra
 * solo necesita indicar QUÉ producto (idProducto) y CUÁNTAS unidades
 * (cantidad). El "subtotal" NO se pide en el request porque no es un
 * dato que el usuario deba escribir a mano: se calcula en la capa de
 * servicio como `producto.precioUnitario * cantidad`, evitando que el
 * cliente pueda enviar un subtotal manipulado que no coincida con el
 * precio real del producto (una validación de integridad que nunca debe
 * delegarse al lado del cliente).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleRequestDTO {

    @NotNull(message = "El producto es obligatorio")
    private Long idProducto;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;
}
