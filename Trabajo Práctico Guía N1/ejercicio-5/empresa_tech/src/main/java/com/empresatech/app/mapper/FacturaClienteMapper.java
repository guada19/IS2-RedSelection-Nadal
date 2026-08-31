package com.empresatech.app.mapper;

import com.empresatech.app.dto.response.FacturaClienteResponseDTO;
import com.empresatech.app.model.Cliente;
import com.empresatech.app.model.EstadoFactura;
import com.empresatech.app.model.FacturaCliente;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper de FacturaCliente.
 *
 * No expone un método `toEntity(FacturaClienteRequestDTO)` simple, sino
 * `crearFacturaCliente(...)`, que recibe explícitamente cada dato ya
 * resuelto por la capa de servicio: el Cliente (buscado por
 * `idCliente`), el número de factura (generado por el sistema, por
 * ejemplo de forma correlativa) y el total (calculado sumando los
 * subtotales de los Detalle ya construidos con DetalleMapper).
 *
 * Esta decisión de diseño es deliberada: un Mapper "puro" no debería
 * decidir reglas de negocio (cómo se numera una factura, cuál es su
 * estado inicial más allá de un valor fijo, cómo se calcula el total a
 * partir de una lista que todavía no existe). Esas decisiones viven en
 * la capa de Servicio; el Mapper solo se encarga de EMPAQUETAR esos
 * datos ya resueltos en la forma de una entidad o de un DTO.
 */
@Component
public class FacturaClienteMapper {

    /**
     * @param nroFactura número ya generado por el servicio (p. ej. de
     *                    forma correlativa consultando la última factura).
     * @param total       suma de los subtotales de `detalles`, ya
     *                    calculada por el servicio antes de llamar a este
     *                    método (los Detalle se agregan a la factura por
     *                    separado, ver FacturaCliente.getDetalles()).
     */
    public FacturaCliente crearFacturaCliente(String nroFactura, Cliente cliente, BigDecimal total) {
        return FacturaCliente.builder()
                .nroFactura(nroFactura)
                .fecha(LocalDate.now())
                .estado(EstadoFactura.PENDIENTE)
                // Toda factura nueva nace en estado PENDIENTE; la
                // transición a PAGADA/ANULADA es una operación de negocio
                // posterior y explícita, no parte del alta.
                .total(total)
                .cliente(cliente)
                .build();
        // Notar que NO se setean los "detalles" acá: dado que Detalle es
        // el lado propietario de la relación (@ManyToOne factura_id, ver
        // DetalleMapper), primero debe existir esta FacturaCliente
        // persistida (con id) para poder construir cada Detalle
        // referenciándola. El orden correcto, coordinado por el
        // servicio, es: 1) crear y guardar la FacturaCliente sin
        // detalles, 2) construir cada Detalle con DetalleMapper pasando
        // esta factura ya persistida, 3) agregar esos Detalle a
        // factura.getDetalles() (o guardarlos vía DetalleRepository).
    }

    public FacturaClienteResponseDTO toResponseDTO(FacturaCliente factura, DetalleMapper detalleMapper) {
        return FacturaClienteResponseDTO.builder()
                .id(factura.getId())
                .nroFactura(factura.getNroFactura())
                .fecha(factura.getFecha())
                .estado(factura.getEstado())
                .total(factura.getTotal())
                .eliminado(factura.isEliminado())
                .clienteId(factura.getCliente().getId())
                .clienteNombreCompleto(factura.getCliente().getNombre() + " " + factura.getCliente().getApellido())
                .detalles(mapearDetalles(factura, detalleMapper))
                .build();
    }

    private List<com.empresatech.app.dto.response.DetalleResponseDTO> mapearDetalles(
            FacturaCliente factura, DetalleMapper detalleMapper) {
        // Se delega en DetalleMapper para no duplicar la lógica de
        // conversión Detalle -> DetalleResponseDTO, ya definida allí.
        return factura.getDetalles().stream()
                .map(detalleMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
