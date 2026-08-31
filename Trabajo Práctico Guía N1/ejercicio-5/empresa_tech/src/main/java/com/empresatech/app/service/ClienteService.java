package com.empresatech.app.service;

import com.empresatech.app.dto.request.ClienteRequestDTO;
import com.empresatech.app.dto.response.ClienteResponseDTO;

import java.util.List;

/**
 * Contrato para el CRUD de clientes con baja lógica.
 */
public interface ClienteService {

    ClienteResponseDTO crearCliente(ClienteRequestDTO dto);

    ClienteResponseDTO modificarCliente(String id, ClienteRequestDTO dto);

    void eliminarCliente(String id);

    ClienteResponseDTO buscarPorId(String id);

    ClienteResponseDTO buscarPorDni(String dni);

    List<ClienteResponseDTO> listarTodos();
}
