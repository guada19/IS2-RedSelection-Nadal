package com.empresatech.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * DTO de entrada para crear/actualizar un Producto.
 *
 * Incluye `cantidadInicial`, que NO es un campo de la entidad Producto,
 * sino un dato de conveniencia para que, al dar de alta un producto
 * nuevo desde un único formulario, el usuario también pueda cargar de
 * una vez la cantidad inicial de stock. Este DTO representa la
 * INTENCIÓN del usuario ("crear este producto con esta cantidad
 * inicial"), no el modelo de datos final: es responsabilidad de la capa
 * de servicio, al recibir este DTO, crear tanto el Producto como su
 * Stock asociado (dos entidades) a partir de un solo objeto de entrada.
 * Esta es una de las ventajas centrales del patrón DTO: el "shape" de
 * los datos de entrada puede diferir del modelo de persistencia sin
 * ningún problema, porque son objetos completamente independientes.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoRequestDTO {

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 150, message = "El nombre no puede superar los 150 caracteres")
    private String nombre;

    @NotNull(message = "El precio unitario es obligatorio")
    @Positive(message = "El precio unitario debe ser mayor a 0")
    // @Positive: rechaza cero y negativos; un producto no puede venderse
    // a precio nulo o negativo.
    private BigDecimal precioUnitario;

    @NotNull(message = "La cantidad inicial de stock es obligatoria")
    @PositiveOrZero(message = "La cantidad inicial no puede ser negativa")
    // @PositiveOrZero (no @Positive): se permite cargar un producto con
    // stock inicial en 0 (por ejemplo, si todavía no llegó la mercadería
    // pero ya se quiere tener el producto cargado en el catálogo).
    private Integer cantidadInicial;
}
