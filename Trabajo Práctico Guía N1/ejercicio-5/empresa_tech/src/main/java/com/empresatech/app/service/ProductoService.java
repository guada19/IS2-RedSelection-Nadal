package com.empresatech.app.service;

import com.empresatech.app.dto.request.ProductoRequestDTO;
import com.empresatech.app.dto.response.ProductoResponseDTO;

import java.util.List;

/**
 * Contrato de servicios para la gestión del catálogo de productos.
 */
public interface ProductoService {

    ProductoResponseDTO crearProducto(ProductoRequestDTO dto);

    ProductoResponseDTO modificarProducto(String id, ProductoRequestDTO dto);

    void eliminarProducto(String id);

    ProductoResponseDTO buscarPorId(String id);

    List<ProductoResponseDTO> listarProductos();
}
