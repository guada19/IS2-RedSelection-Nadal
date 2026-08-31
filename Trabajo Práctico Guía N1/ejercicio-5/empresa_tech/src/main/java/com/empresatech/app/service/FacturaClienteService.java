package com.empresatech.app.service;

import com.empresatech.app.dto.request.FacturaClienteRequestDTO;
import com.empresatech.app.dto.response.FacturaClienteResponseDTO;

import java.util.List;

/**
 * Contrato para la gestión de ventas y facturación a clientes.
 */
public interface FacturaClienteService {

    FacturaClienteResponseDTO registrarFacturaCliente(FacturaClienteRequestDTO dto);

    List<FacturaClienteResponseDTO> listarFacturasCliente();

    FacturaClienteResponseDTO buscarPorId(String id);
}
