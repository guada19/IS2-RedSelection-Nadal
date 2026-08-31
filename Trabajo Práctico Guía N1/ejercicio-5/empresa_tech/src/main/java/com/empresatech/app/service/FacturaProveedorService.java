package com.empresatech.app.service;

import com.empresatech.app.dto.request.FacturaProveedorRequestDTO;
import com.empresatech.app.dto.response.FacturaProveedorResponseDTO;

import java.util.List;

/**
 * Contrato para la gestión de compras y facturación a proveedores.
 */
public interface FacturaProveedorService {

    FacturaProveedorResponseDTO registrarFacturaProveedor(FacturaProveedorRequestDTO dto);

    List<FacturaProveedorResponseDTO> listarFacturasProveedor();

    FacturaProveedorResponseDTO buscarPorId(String id);
}
