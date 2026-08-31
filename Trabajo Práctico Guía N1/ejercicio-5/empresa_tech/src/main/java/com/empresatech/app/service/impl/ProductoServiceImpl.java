package com.empresatech.app.service.impl;

import com.empresatech.app.dto.request.ProductoRequestDTO;
import com.empresatech.app.dto.response.ProductoResponseDTO;
import com.empresatech.app.mapper.ProductoMapper;
import com.empresatech.app.mapper.StockMapper;
import com.empresatech.app.model.Producto;
import com.empresatech.app.model.Stock;
import com.empresatech.app.repository.ProductoRepository;
import com.empresatech.app.repository.StockRepository;
import com.empresatech.app.exception.ResourceNotFoundException;
import com.empresatech.app.service.ProductoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación de los casos de uso del catálogo de productos.
 */
@Service
@Transactional
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final StockRepository stockRepository;
    private final ProductoMapper productoMapper;
    private final StockMapper stockMapper;

    public ProductoServiceImpl(ProductoRepository productoRepository,
                              StockRepository stockRepository,
                              ProductoMapper productoMapper,
                              StockMapper stockMapper) {
        this.productoRepository = productoRepository;
        this.stockRepository = stockRepository;
        this.productoMapper = productoMapper;
        this.stockMapper = stockMapper;
    }

    @Override
    public ProductoResponseDTO crearProducto(ProductoRequestDTO dto) {
        Producto producto = productoMapper.toEntity(dto);
        Producto guardado = productoRepository.save(producto);

        Stock stockInicial = stockMapper.crearStockInicial(dto.getCantidadInicial(), guardado);
        guardado.setStock(stockInicial);
        stockRepository.save(stockInicial);

        return productoMapper.toResponseDTO(guardado);
    }

    @Override
    public ProductoResponseDTO modificarProducto(String id, ProductoRequestDTO dto) {
        Producto producto = buscarEntidadPorId(id);
        productoMapper.actualizarEntidadDesdeDTO(dto, producto);
        return productoMapper.toResponseDTO(productoRepository.save(producto));
    }

    @Override
    public void eliminarProducto(String id) {
        Producto producto = buscarEntidadPorId(id);
        producto.setEliminado(true);
        productoRepository.save(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponseDTO buscarPorId(String id) {
        return productoMapper.toResponseDTO(buscarEntidadPorId(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> listarProductos() {
        return productoRepository.findByEliminadoFalse()
                .stream()
                .map(productoMapper::toResponseDTO)
                .toList();
    }

    private Producto buscarEntidadPorId(String id) {
        Long productoId = parseId(id, "producto");
        return productoRepository.findById(productoId)
                .filter(producto -> !producto.isEliminado())
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
    }

    private Long parseId(String rawId, String nombreEntidad) {
        try {
            return Long.valueOf(rawId);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("El id de " + nombreEntidad + " es inválido: " + rawId, ex);
        }
    }
}
