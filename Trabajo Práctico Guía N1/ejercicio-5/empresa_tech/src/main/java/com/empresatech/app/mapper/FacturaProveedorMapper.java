package com.empresatech.app.mapper;

import com.empresatech.app.dto.response.DetalleResponseDTO;
import com.empresatech.app.dto.response.FacturaProveedorResponseDTO;
import com.empresatech.app.model.EstadoFactura;
import com.empresatech.app.model.FacturaProveedor;
import com.empresatech.app.model.Proveedor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper de FacturaProveedor. Simétrico a FacturaClienteMapper; ver esa
 * clase para la justificación de por qué `crearFacturaProveedor` recibe
 * datos ya resueltos (Proveedor, número, total) en lugar de partir
 * directamente de FacturaProveedorRequestDTO.
 */
@Component
public class FacturaProveedorMapper {

    public FacturaProveedor crearFacturaProveedor(String nroFactura, Proveedor proveedor, BigDecimal total) {
        return FacturaProveedor.builder()
                .nroFactura(nroFactura)
                .fecha(LocalDate.now())
                .estado(EstadoFactura.PENDIENTE)
                .total(total)
                .proveedor(proveedor)
                .build();
        // Igual que en FacturaClienteMapper: los Detalle se construyen y
        // asocian DESPUÉS de persistir esta factura, ya que necesitan su
        // id para completar la relación @ManyToOne factura_id.
    }

    public FacturaProveedorResponseDTO toResponseDTO(FacturaProveedor factura, DetalleMapper detalleMapper) {
        return FacturaProveedorResponseDTO.builder()
                .id(factura.getId())
                .nroFactura(factura.getNroFactura())
                .fecha(factura.getFecha())
                .estado(factura.getEstado())
                .total(factura.getTotal())
                .eliminado(factura.isEliminado())
                .proveedorId(factura.getProveedor().getId())
                .proveedorRazonSocial(factura.getProveedor().getRazonSocial())
                .detalles(mapearDetalles(factura, detalleMapper))
                .build();
    }

    private List<DetalleResponseDTO> mapearDetalles(FacturaProveedor factura, DetalleMapper detalleMapper) {
        return factura.getDetalles().stream()
                .map(detalleMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
