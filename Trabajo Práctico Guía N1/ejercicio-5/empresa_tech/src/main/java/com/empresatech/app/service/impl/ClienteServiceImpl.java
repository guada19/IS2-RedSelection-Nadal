package com.empresatech.app.service.impl;

import com.empresatech.app.dto.request.ClienteRequestDTO;
import com.empresatech.app.dto.response.ClienteResponseDTO;
import com.empresatech.app.mapper.ClienteMapper;
import com.empresatech.app.model.Cliente;
import com.empresatech.app.repository.ClienteRepository;
import com.empresatech.app.exception.ResourceNotFoundException;
import com.empresatech.app.service.ClienteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación del CRUD de clientes con baja lógica.
 */
@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    public ClienteServiceImpl(ClienteRepository clienteRepository, ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
    }

    @Override
    public ClienteResponseDTO crearCliente(ClienteRequestDTO dto) {
        Cliente cliente = clienteMapper.toEntity(dto);
        return clienteMapper.toResponseDTO(clienteRepository.save(cliente));
    }

    @Override
    public ClienteResponseDTO modificarCliente(String id, ClienteRequestDTO dto) {
        Cliente cliente = buscarEntidadPorId(id);
        clienteMapper.actualizarEntidadDesdeDTO(dto, cliente);
        return clienteMapper.toResponseDTO(clienteRepository.save(cliente));
    }

    @Override
    public void eliminarCliente(String id) {
        Cliente cliente = buscarEntidadPorId(id);
        cliente.setEliminado(true);
        clienteRepository.save(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponseDTO buscarPorId(String id) {
        return clienteMapper.toResponseDTO(buscarEntidadPorId(id));
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponseDTO buscarPorDni(String dni) {
        Cliente cliente = clienteRepository.findByDni(dni)
                .filter(item -> !item.isEliminado())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con DNI: " + dni));
        return clienteMapper.toResponseDTO(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> listarTodos() {
        return clienteRepository.findByEliminadoFalse()
                .stream()
                .map(clienteMapper::toResponseDTO)
                .toList();
    }

    private Cliente buscarEntidadPorId(String id) {
        Long clienteId = parseId(id, "cliente");
        return clienteRepository.findById(clienteId)
                .filter(item -> !item.isEliminado())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));
    }

    private Long parseId(String rawId, String nombreEntidad) {
        try {
            return Long.valueOf(rawId);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("El id de " + nombreEntidad + " es inválido: " + rawId, ex);
        }
    }
}
