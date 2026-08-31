package com.empresatech.app.mapper;

import com.empresatech.app.dto.response.StockResponseDTO;
import com.empresatech.app.model.Producto;
import com.empresatech.app.model.Stock;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Mapper de Stock.
 *
 * No existe un `toEntity(StockRequestDTO)` porque, según los requisitos,
 * el Stock no se crea/edita a través de un formulario propio: nace junto
 * con el Producto (a partir de `ProductoRequestDTO.cantidadInicial`, ver
 * ProductoMapper) y sus actualizaciones posteriores de cantidad
 * corresponden a operaciones de negocio específicas (una venta que
 * descuenta stock, una compra que lo repone), no a una edición libre de
 * campos. Por eso este Mapper solo expone la dirección Entidad -> DTO.
 */
@Component
public class StockMapper {

    public StockResponseDTO toResponseDTO(Stock stock) {
        return StockResponseDTO.builder()
                .id(stock.getId())
                .cantidad(stock.getCantidad())
                .actualizacion(stock.getActualizacion())
                .build();
    }

    /**
     * Crea el Stock inicial de un Producto recién dado de alta. Recibe
     * el Producto ya persistido (o al menos ya construido) como
     * parámetro en lugar de resolverlo por id, porque este método se
     * invoca desde la capa de servicio en el mismo flujo transaccional
     * en el que se está creando el propio Producto: no tiene sentido
     * volver a consultarlo en la base de datos si ya se tiene la
     * instancia en memoria.
     */
    public Stock crearStockInicial(Integer cantidadInicial, Producto producto) {
        return Stock.builder()
                .cantidad(cantidadInicial)
                .actualizacion(LocalDateTime.now())
                .producto(producto)
                .build();
    }
}
