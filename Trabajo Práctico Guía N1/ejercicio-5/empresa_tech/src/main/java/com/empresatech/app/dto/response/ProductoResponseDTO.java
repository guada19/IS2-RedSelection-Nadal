package com.empresatech.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * DTO de salida para un Producto, incluyendo su Stock anidado.
 *
 * A diferencia del enfoque de exponer solo un "stockId" (una alternativa
 * válida), aquí se decide anidar el StockResponseDTO COMPLETO porque:
 *   - Es exactamente lo que necesita, por ejemplo, una grilla de
 *     catálogo de productos: nombre, precio y cantidad disponible en una
 *     sola consulta/respuesta, sin que la vista tenga que hacer una
 *     segunda petición para conocer el stock.
 *   - Al ser un Response (no un Request), no hay riesgo de ambigüedad de
 *     validación por anidar objetos completos.
 *   - Como se explicó en StockResponseDTO, este NO vuelve a referenciar
 *     al producto, por lo que anidarlo aquí no genera ningún ciclo.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoResponseDTO {

    private Long id;
    private String nombre;
    private BigDecimal precioUnitario;
    private boolean eliminado;

    /**
     * Puede llegar en null si el producto todavía no tiene un Stock
     * asociado (por ejemplo, en un estado transitorio antes de que la
     * capa de servicio lo cree); la vista debe contemplar ese caso.
     */
    private StockResponseDTO stock;
}
