package com.empresatech.app.service;

import com.empresatech.app.dto.request.ProveedorRequestDTO;
import com.empresatech.app.dto.response.ProveedorResponseDTO;

import java.util.List;

/**
 * Contrato para el CRUD de proveedores con baja lógica.
 */
public interface ProveedorService {

    ProveedorResponseDTO crearProveedor(ProveedorRequestDTO dto);

    ProveedorResponseDTO modificarProveedor(String id, ProveedorRequestDTO dto);

    void eliminarProveedor(String id);

    ProveedorResponseDTO buscarPorId(String id);

    ProveedorResponseDTO buscarPorCuit(String cuit);

    List<ProveedorResponseDTO> listarTodos();
}
