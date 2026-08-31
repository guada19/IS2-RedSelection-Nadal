package com.empresatech.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad abstracta que representa una Factura genérica.
 *
 * Es la clase padre de la jerarquía de herencia: FacturaCliente (venta a
 * un cliente) y FacturaProveedor (compra a un proveedor) heredan de esta
 * clase todos sus campos y comportamiento comunes.
 *
 * @MappedSuperclass NO se usa aquí a propósito: se eligió herencia real de
 * entidades JPA (@Inheritance) porque se necesita poder consultar TODAS
 * las facturas (de cliente y de proveedor) de manera polimórfica a través
 * de un único FacturaRepository<Factura>, algo que @MappedSuperclass no
 * permite (esa anotación no crea una entidad "consultable" en sí misma).
 */
@Entity
// @Entity en una clase abstracta: sigue siendo necesaria para que
// Hibernate reconozca esta clase como la raíz de la jerarquía de
// herencia y genere el mapeo correspondiente.
@Table(name = "facturas")
// Con la estrategia SINGLE_TABLE, TODAS las subclases (FacturaCliente,
// FacturaProveedor) se almacenan en esta única tabla "facturas". Las
// columnas específicas de cada subclase (proveedor_id, cliente_id)
// quedan como NULL en las filas que no correspondan a esa subclase.
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
// @Inheritance define la estrategia de mapeo de la jerarquía de
// herencia a tablas relacionales. Se eligió SINGLE_TABLE (en lugar de
// JOINED) porque:
//   - Es la estrategia de MEJOR RENDIMIENTO en lectura: para traer una
//     factura completa no hace falta ningún JOIN entre tablas.
//   - La jerarquía es simple (solo 2 subclases con pocas columnas extra
//     cada una), por lo que el "desperdicio" de columnas NULL es mínimo.
//   - Simplifica las consultas polimórficas (listar todas las facturas
//     sin importar el tipo).
@DiscriminatorColumn(name = "tipo_factura", discriminatorType = DiscriminatorType.STRING)
// @DiscriminatorColumn: en SINGLE_TABLE, Hibernate necesita una columna
// extra para saber, al leer una fila, a qué subclase concreta
// corresponde (FacturaCliente o FacturaProveedor). Esa columna es
// "tipo_factura" y se guarda como texto (STRING) para que sea legible
// directamente en la base de datos.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
// @SuperBuilder (Lombok): variante de @Builder pensada para jerarquías
// de herencia. A diferencia de @Builder, genera un builder que también
// puede construir e inicializar los campos heredados de esta clase
// padre desde las subclases (FacturaCliente.builder()... también podrá
// setear nroFactura, fecha, etc.).
public abstract class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nro_factura", nullable = false, unique = true, length = 30)
    // Número de factura visible/legal (distinto del ID técnico interno),
    // por ejemplo con formato "0001-00001234". unique=true evita
    // duplicados.
    private String nroFactura;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Enumerated(EnumType.STRING)
    // @Enumerated(EnumType.STRING): guarda el estado como texto
    // ("PENDIENTE", "PAGADA", "ANULADA") en lugar del índice numérico
    // (EnumType.ORDINAL), lo cual es más legible y seguro ante futuros
    // cambios en el orden de los valores del enum.
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoFactura estado;

    @Column(name = "total", nullable = false, precision = 14, scale = 2)
    // Monto total de la factura (suma de los subtotales de sus Detalles).
    // Se recalcula normalmente en la capa de servicio, no aquí.
    private BigDecimal total;

    @Column(name = "eliminado", nullable = false)
    // Borrado lógico también aplicado a facturas: una factura NUNCA
    // debería eliminarse físicamente por razones contables/legales, solo
    // marcarse como eliminada/anulada.
    @lombok.Builder.Default
    private boolean eliminado = false;

    /**
     * Relación 1:N con Detalle. Una Factura contiene varias líneas de
     * Detalle (una por cada producto vendido/comprado en esa factura).
     *
     * mappedBy = "factura": el lado propietario de la relación (la
     * columna FK "factura_id") vive en la entidad Detalle.
     *
     * cascade = CascadeType.ALL: al guardar, actualizar o eliminar una
     * Factura, la operación se propaga a todos sus Detalles asociados
     * (tiene sentido: un Detalle no existe sin su Factura).
     *
     * orphanRemoval = true: si se quita un Detalle de esta lista (por
     * ejemplo, factura.getDetalles().remove(detalle)), Hibernate lo
     * elimina automáticamente de la base de datos en lugar de dejarlo
     * "huérfano" (sin factura asociada).
     *
     * fetch = FetchType.LAZY: los detalles no se cargan automáticamente
     * junto con la factura; se cargan solo al acceder explícitamente a
     * factura.getDetalles(), evitando consultas costosas innecesarias.
     */
    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @lombok.Builder.Default
    private List<Detalle> detalles = new ArrayList<>();
    // Se inicializa la lista vacía (en vez de dejarla en null) para
    // evitar NullPointerException al intentar agregar detalles a una
    // factura recién creada.
}
