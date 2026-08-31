package com.empresatech.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO de salida para el Stock de un Producto.
 *
 * No incluye una referencia de vuelta al producto (ni su id ni un
 * ProductoResponseDTO anidado): este DTO está pensado para viajar
 * SIEMPRE dentro de un ProductoResponseDTO (ver más abajo), nunca de
 * forma suelta. Evitar la referencia inversa aquí es lo que rompe el
 * ciclo Producto -> Stock -> Producto -> ... que sí existe a nivel de
 * entidades JPA (relación bidireccional @OneToOne).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockResponseDTO {

    private Long id;
    private Integer cantidad;
    private LocalDateTime actualizacion;
}
