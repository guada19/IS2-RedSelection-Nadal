package com.empresatech.app.dto.response;

import com.empresatech.app.model.EstadoFactura;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO de salida para una Factura de Proveedor (compra). Estructuralmente
 * simétrico a FacturaClienteResponseDTO, exponiendo "proveedorRazonSocial"
 * ya resuelto en lugar del Proveedor completo o de un id suelto.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacturaProveedorResponseDTO {

    private Long id;
    private String nroFactura;
    private LocalDate fecha;
    private EstadoFactura estado;
    private BigDecimal total;
    private boolean eliminado;

    private Long proveedorId;
    private String proveedorRazonSocial;

    private List<DetalleResponseDTO> detalles;
}
