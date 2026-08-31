package com.empresatech.app.mapper;

import com.empresatech.app.dto.request.DetalleRequestDTO;
import com.empresatech.app.dto.response.DetalleResponseDTO;
import com.empresatech.app.model.Detalle;
import com.empresatech.app.model.Factura;
import com.empresatech.app.model.Producto;
import org.springframework.stereotype.Component;

/**
 * Mapper de Detalle.
 *
 * A diferencia de ClienteMapper/ProveedorMapper, `toEntity` no puede
 * construirse solo a partir del DTO: un Detalle necesita una referencia
 * real a un Producto (para @ManyToOne) y a una Factura. Resolver esas
 * referencias (buscar el Producto por `idProducto` en la base de datos)
 * excede la responsabilidad de un Mapper, que debe ser una clase "pura"
 * de transformación de datos, sin dependencias de repositorios ni acceso
 * a la base de datos. Por eso el Producto (ya resuelto por la capa de
 * servicio a partir de `dto.getIdProducto()`) y la Factura se reciben
 * como parámetros adicionales.
 */
@Component
public class DetalleMapper {

    /**
     * Construye un Detalle a partir del Request DTO, el Producto ya
     * resuelto (buscado por id en la capa de servicio) y la Factura a la
     * que pertenece.
     *
     * El "subtotal" se calcula acá, multiplicando el precio unitario
     * ACTUAL del producto por la cantidad pedida, en lugar de aceptarlo
     * como dato de entrada (recordar que DetalleRequestDTO no lo
     * incluye, ver su documentación). Esto garantiza que el subtotal
     * siempre sea consistente con el precio real del producto en el
     * momento de la operación, sin depender de un valor que el cliente
     * pudiera enviar manipulado.
     */
    public Detalle toEntity(DetalleRequestDTO dto, Producto producto, Factura factura) {
        return Detalle.builder()
                .cantidad(dto.getCantidad())
                .subtotal(producto.getPrecioUnitario().multiply(java.math.BigDecimal.valueOf(dto.getCantidad())))
                .producto(producto)
                .factura(factura)
                .build();
    }

    public DetalleResponseDTO toResponseDTO(Detalle detalle) {
        return DetalleResponseDTO.builder()
                .id(detalle.getId())
                .productoNombre(detalle.getProducto().getNombre())
                .precioUnitario(detalle.getProducto().getPrecioUnitario())
                .cantidad(detalle.getCantidad())
                .subtotal(detalle.getSubtotal())
                .build();
        // Nota: `detalle.getProducto()` se invoca acá dentro de la capa
        // de servicio, mientras la sesión de Hibernate sigue activa, por
        // lo que acceder a esta relación @ManyToOne LAZY es seguro. Es
        // precisamente el patrón que evita el LazyInitializationException
        // que ocurriría si esta misma llamada se hiciera después, ya
        // en la vista Thymeleaf.
    }
}
