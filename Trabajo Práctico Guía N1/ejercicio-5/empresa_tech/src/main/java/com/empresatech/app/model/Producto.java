package com.empresatech.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Entidad que representa un producto tecnológico del catálogo de la
 * tienda (por ejemplo: un mouse, un monitor, una notebook, etc.).
 *
 * Un Producto mantiene una relación 1:1 con Stock, ya que cada producto
 * tiene un único registro de inventario asociado.
 */
@Entity
// @Entity: le indica a Hibernate que esta clase es una entidad JPA, es
// decir, que debe mapearse a una tabla de la base de datos y que sus
// instancias representan filas de esa tabla.
@Table(name = "productos")
// @Table: especifica el nombre exacto de la tabla en MySQL. Si se omitiera,
// Hibernate usaría por defecto el nombre de la clase ("Producto").
@Getter
@Setter
// @Getter/@Setter (Lombok): generan automáticamente en tiempo de
// compilación los métodos getX()/setX() para todos los campos, evitando
// escribirlos manualmente.
@NoArgsConstructor
// @NoArgsConstructor (Lombok): genera un constructor sin argumentos,
// OBLIGATORIO para JPA/Hibernate, ya que internamente instancia las
// entidades por reflexión antes de poblar sus campos.
@AllArgsConstructor
// @AllArgsConstructor (Lombok): genera un constructor con todos los
// campos como parámetros, útil junto con @Builder.
@Builder
// @Builder (Lombok): genera un patrón "builder" (Producto.builder()...build())
// que facilita crear instancias de forma legible, especialmente cuando hay
// muchos campos.
public class Producto {

    @Id
    // @Id: marca este campo como la clave primaria (Primary Key) de la tabla.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @GeneratedValue: indica que el valor del ID lo genera automáticamente
    // la base de datos. IDENTITY delega en la columna auto-incremental
    // nativa de MySQL (AUTO_INCREMENT).
    private Long id;

    @Column(name = "nombre", nullable = false, length = 150)
    // @Column: personaliza el mapeo de este atributo a la columna de la
    // tabla. nullable=false -> la columna no admite NULL (obligatoria);
    // length=150 -> define el tamaño máximo del VARCHAR generado.
    private String nombre;

    @Column(name = "precio_unitario", nullable = false, precision = 12, scale = 2)
    // precision/scale controlan cuántos dígitos totales (12) y cuántos de
    // ellos van después de la coma (2) tiene la columna DECIMAL generada.
    // Se usa BigDecimal (no double/float) para evitar errores de
    // redondeo en cálculos monetarios.
    private BigDecimal precioUnitario;

    @Column(name = "eliminado", nullable = false)
    // Campo utilizado para implementar "borrado lógico" (soft delete):
    // en lugar de eliminar físicamente la fila de la base de datos, se
    // marca como eliminado=true, preservando el historial (por ejemplo,
    // para no perder la trazabilidad de ventas ya facturadas).
    @Builder.Default
    private boolean eliminado = false;

    /**
     * Relación 1:1 con Stock. mappedBy="producto" indica que la clave
     * foránea (la columna que realmente contiene la relación en la base
     * de datos) vive del lado de la entidad Stock, en su campo "producto".
     * Producto es, por lo tanto, el lado "inverso" (no propietario) de
     * la relación.
     *
     * cascade = CascadeType.ALL: cualquier operación (persistir, actualizar,
     * eliminar) que se haga sobre un Producto se propaga automáticamente
     * a su Stock asociado (tiene sentido: el stock no existe sin el
     * producto).
     *
     * orphanRemoval = true: si se desasocia el Stock de este Producto
     * (por ejemplo, se le asigna null), Hibernate elimina automáticamente
     * ese registro de Stock huérfano de la base de datos.
     *
     * fetch = FetchType.LAZY: el Stock NO se carga automáticamente al
     * traer un Producto desde la base de datos; solo se consulta cuando
     * se accede explícitamente a producto.getStock(). Mejora el
     * rendimiento evitando joins innecesarios.
     */
    @OneToOne(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Stock stock;
}
