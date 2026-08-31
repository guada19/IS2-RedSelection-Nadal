package com.empresatech.app.service.impl;

import com.empresatech.app.dto.request.ProveedorRequestDTO;
import com.empresatech.app.dto.response.ProveedorResponseDTO;
import com.empresatech.app.mapper.ProveedorMapper;
import com.empresatech.app.model.Proveedor;
import com.empresatech.app.repository.ProveedorRepository;
import com.empresatech.app.exception.ResourceNotFoundException;
import com.empresatech.app.service.ProveedorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación del CRUD de proveedores con baja lógica.
 */
@Service
@Transactional
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository proveedorRepository;
    private final ProveedorMapper proveedorMapper;

    public ProveedorServiceImpl(ProveedorRepository proveedorRepository, ProveedorMapper proveedorMapper) {
        this.proveedorRepository = proveedorRepository;
        this.proveedorMapper = proveedorMapper;
    }

    @Override
    public ProveedorResponseDTO crearProveedor(ProveedorRequestDTO dto) {
        Proveedor proveedor = proveedorMapper.toEntity(dto);
        return proveedorMapper.toResponseDTO(proveedorRepository.save(proveedor));
    }

    @Override
    public ProveedorResponseDTO modificarProveedor(String id, ProveedorRequestDTO dto) {
        Proveedor proveedor = buscarEntidadPorId(id);
        proveedorMapper.actualizarEntidadDesdeDTO(dto, proveedor);
        return proveedorMapper.toResponseDTO(proveedorRepository.save(proveedor));
    }

    @Override
    public void eliminarProveedor(String id) {
        Proveedor proveedor = buscarEntidadPorId(id);
        proveedor.setActivo(false);
        proveedorRepository.save(proveedor);
    }

    @Override
    @Transactional(readOnly = true)
    public ProveedorResponseDTO buscarPorId(String id) {
        return proveedorMapper.toResponseDTO(buscarEntidadPorId(id));
    }

    @Override
    @Transactional(readOnly = true)
    public ProveedorResponseDTO buscarPorCuit(String cuit) {
        Proveedor proveedor = proveedorRepository.findByCuit(cuit)
                .filter(item -> item.isActivo())
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con CUIT: " + cuit));
        return proveedorMapper.toResponseDTO(proveedor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProveedorResponseDTO> listarTodos() {
        return proveedorRepository.findByActivoTrue()
                .stream()
                .map(proveedorMapper::toResponseDTO)
                .toList();
    }

    private Proveedor buscarEntidadPorId(String id) {
        Long proveedorId = parseId(id, "proveedor");
        return proveedorRepository.findById(proveedorId)
                .filter(item -> item.isActivo())
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con id: " + id));
    }

    private Long parseId(String rawId, String nombreEntidad) {
        try {
            return Long.valueOf(rawId);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("El id de " + nombreEntidad + " es inválido: " + rawId, ex);
        }
    }
}
