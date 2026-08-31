package com.empresatech.app.service;

import com.empresatech.app.dto.response.StockResponseDTO;

/**
 * Contrato dedicado a la consulta y actualización del stock por producto.
 */
public interface StockService {

    StockResponseDTO consultarStock(String productoId);

    StockResponseDTO incrementarStock(String productoId, int cantidad);

    StockResponseDTO disminuirStock(String productoId, int cantidad);
}
