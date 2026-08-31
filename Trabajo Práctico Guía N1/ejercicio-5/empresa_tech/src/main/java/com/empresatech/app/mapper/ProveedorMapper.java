package com.empresatech.app.mapper;

import com.empresatech.app.dto.request.ProveedorRequestDTO;
import com.empresatech.app.dto.response.ProveedorResponseDTO;
import com.empresatech.app.model.Proveedor;
import org.springframework.stereotype.Component;

/**
 * Mapper de Proveedor <-> sus DTOs. Ver ClienteMapper para la
 * explicación general del patrón DTO + Mapper.
 */
@Component
public class ProveedorMapper {

    /**
     * "activo" se fija explícitamente en `true`, ya que todo proveedor
     * nuevo se da de alta activo por defecto; ese campo no forma parte
     * del Request DTO (ver ProveedorRequestDTO) porque es una decisión
     * de negocio, no un dato que el usuario deba cargar.
     */
    public Proveedor toEntity(ProveedorRequestDTO dto) {
        return Proveedor.builder()
                .cuit(dto.getCuit())
                .razonSocial(dto.getRazonSocial())
                .telefono(dto.getTelefono())
                .activo(true)
                .build();
    }

    public ProveedorResponseDTO toResponseDTO(Proveedor proveedor) {
        return ProveedorResponseDTO.builder()
                .id(proveedor.getId())
                .cuit(proveedor.getCuit())
                .razonSocial(proveedor.getRazonSocial())
                .telefono(proveedor.getTelefono())
                .activo(proveedor.isActivo())
                .build();
    }

    /**
     * Actualiza los datos editables de un Proveedor existente. No toca
     * "activo": esa baja/reactivación se maneja con una operación propia
     * (por ejemplo PATCH /proveedores/{id}/estado), no mezclada con la
     * edición general de datos.
     */
    public void actualizarEntidadDesdeDTO(ProveedorRequestDTO dto, Proveedor proveedor) {
        proveedor.setCuit(dto.getCuit());
        proveedor.setRazonSocial(dto.getRazonSocial());
        proveedor.setTelefono(dto.getTelefono());
    }
}
