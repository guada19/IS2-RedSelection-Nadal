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
 * DTO de salida para una Factura de Cliente (venta), con sus detalles
 * anidados como lista de DetalleResponseDTO.
 *
 * En lugar de exponer un "clienteId" suelto, se expone
 * "clienteNombreCompleto" ya formateado (nombre + apellido concatenados
 * en la capa de mapeo): es un ejemplo directo de una de las ventajas del
 * DTO frente a la entidad, que consiste en poder dar forma a los datos
 * EXACTAMENTE como los necesita la vista (en este caso, un único string
 * legible en lugar de dos campos separados o de un objeto Cliente
 * completo), sin tener que resolver ese formateo del lado de la plantilla
 * Thymeleaf.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacturaClienteResponseDTO {

    private Long id;
    private String nroFactura;
    private LocalDate fecha;
    private EstadoFactura estado;
    private BigDecimal total;
    private boolean eliminado;

    private Long clienteId;
    private String clienteNombreCompleto;

    private List<DetalleResponseDTO> detalles;
}
