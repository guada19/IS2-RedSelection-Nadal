package com.empresatech.app.service.impl;

import com.empresatech.app.dto.response.StockResponseDTO;
import com.empresatech.app.model.Stock;
import com.empresatech.app.repository.StockRepository;
import com.empresatech.app.exception.InsufficientStockException;
import com.empresatech.app.exception.ResourceNotFoundException;
import com.empresatech.app.service.StockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Implementación de la lógica de inventario.
 */
@Service
@Transactional
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;
    private final com.empresatech.app.mapper.StockMapper stockMapper;

    public StockServiceImpl(StockRepository stockRepository,
                           com.empresatech.app.mapper.StockMapper stockMapper) {
        this.stockRepository = stockRepository;
        this.stockMapper = stockMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public StockResponseDTO consultarStock(String productoId) {
        Stock stock = buscarStockPorProductoId(productoId);
        return stockMapper.toResponseDTO(stock);
    }

    @Override
    public StockResponseDTO incrementarStock(String productoId, int cantidad) {
        validarCantidad(cantidad, "incremento");
        Stock stock = buscarStockPorProductoId(productoId);
        stock.setCantidad(stock.getCantidad() + cantidad);
        stock.setActualizacion(LocalDateTime.now());
        return stockMapper.toResponseDTO(stockRepository.save(stock));
    }

    @Override
    public StockResponseDTO disminuirStock(String productoId, int cantidad) {
        validarCantidad(cantidad, "descuento");
        Stock stock = buscarStockPorProductoId(productoId);

        if (cantidad > stock.getCantidad()) {
            throw new InsufficientStockException(
                    "No hay stock suficiente para el producto " + productoId + ". Disponible: " + stock.getCantidad() + ", solicitado: " + cantidad
            );
        }

        stock.setCantidad(stock.getCantidad() - cantidad);
        stock.setActualizacion(LocalDateTime.now());
        return stockMapper.toResponseDTO(stockRepository.save(stock));
    }

    private Stock buscarStockPorProductoId(String productoId) {
        Long id = parseId(productoId, "producto");
        return stockRepository.findByProductoId(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe stock para el producto con id: " + productoId));
    }

    private void validarCantidad(int cantidad, String operacion) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad para " + operacion + " debe ser mayor que cero.");
        }
    }

    private Long parseId(String rawId, String nombreEntidad) {
        try {
            return Long.valueOf(rawId);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("El id de " + nombreEntidad + " es inválido: " + rawId, ex);
        }
    }
}
