package com.empresatech.app.mapper;

import com.empresatech.app.dto.request.ProductoRequestDTO;
import com.empresatech.app.dto.response.ProductoResponseDTO;
import com.empresatech.app.model.Producto;
import org.springframework.stereotype.Component;

/**
 * Mapper de Producto.
 *
 * Depende de StockMapper para poder anidar un StockResponseDTO dentro
 * del ProductoResponseDTO (ver la justificación de ese anidado en la
 * clase ProductoResponseDTO). Es un ejemplo de "composición de mappers":
 * cada Mapper resuelve su propia entidad y delega en el Mapper
 * correspondiente para las relaciones anidadas, en lugar de duplicar esa
 * lógica de conversión.
 */
@Component
public class ProductoMapper {

    private final StockMapper stockMapper;

    // Inyección por constructor (manual en vez de con Lombok
    // @RequiredArgsConstructor, para dejar explícito en el código el
    // único punto donde este Mapper depende de otro).
    public ProductoMapper(StockMapper stockMapper) {
        this.stockMapper = stockMapper;
    }

    /**
     * Construye únicamente la entidad Producto (sin su Stock). La
     * creación del Stock asociado, a partir de `cantidadInicial`, es
     * responsabilidad de la capa de servicio en conjunto con
     * StockMapper.crearStockInicial(...), ya que requiere el Producto
     * YA GUARDADO (con id asignado) para poder completar la relación
     * @OneToOne, algo que este método, al no tener acceso al
     * ProductoRepository, no puede garantizar.
     */
    public Producto toEntity(ProductoRequestDTO dto) {
        return Producto.builder()
                .nombre(dto.getNombre())
                .precioUnitario(dto.getPrecioUnitario())
                .build();
    }

    /**
     * Convierte un Producto (con su Stock ya cargado dentro de la misma
     * transacción/sesión) a su DTO de salida, incluyendo el Stock
     * anidado. Si `producto.getStock()` fuera null (producto sin stock
     * asociado todavía), el campo `stock` del DTO resultante queda en
     * null también; la vista debe contemplar ese caso.
     */
    public ProductoResponseDTO toResponseDTO(Producto producto) {
        return ProductoResponseDTO.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .precioUnitario(producto.getPrecioUnitario())
                .eliminado(producto.isEliminado())
                .stock(producto.getStock() != null ? stockMapper.toResponseDTO(producto.getStock()) : null)
                .build();
    }

    public void actualizarEntidadDesdeDTO(ProductoRequestDTO dto, Producto producto) {
        producto.setNombre(dto.getNombre());
        producto.setPrecioUnitario(dto.getPrecioUnitario());
        // La cantidad de stock NO se actualiza acá: una edición de
        // nombre/precio no debería, como efecto secundario, pisar la
        // cantidad real de inventario. El ajuste de stock se maneja con
        // una operación de negocio explícita y propia.
    }
}
