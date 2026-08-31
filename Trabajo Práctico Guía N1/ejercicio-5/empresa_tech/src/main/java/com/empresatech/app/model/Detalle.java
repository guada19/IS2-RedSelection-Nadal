package com.empresatech.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Entidad que representa una línea de detalle dentro de una Factura
 * (tanto de Cliente como de Proveedor): qué Producto se vendió/compró,
 * en qué cantidad y por qué subtotal.
 *
 * Es la entidad "puente" entre Producto y Factura, ya que una factura
 * puede tener múltiples productos y un producto puede aparecer en
 * múltiples facturas a lo largo del tiempo (relación N:M resuelta
 * explícitamente mediante esta tabla intermedia con atributos propios:
 * cantidad y subtotal).
 */
@Entity
@Table(name = "detalles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Detalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cantidad", nullable = false)
    // Cantidad de unidades del producto vendidas/compradas en esta línea.
    private Integer cantidad;

    @Column(name = "subtotal", nullable = false, precision = 14, scale = 2)
    // Resultado de cantidad * precioUnitario del producto en el momento
    // de la operación (se guarda "congelado" para no depender de que el
    // precio del producto pueda cambiar más adelante).
    private BigDecimal subtotal;

    @Column(name = "eliminado", nullable = false)
    // Borrado lógico de la línea de detalle (por ejemplo, si se anula un
    // ítem puntual de la factura sin anular la factura completa).
    @Builder.Default
    private boolean eliminado = false;

    /**
     * Relación N:1 con Producto: muchos Detalles pueden referenciar el
     * mismo Producto (a lo largo de distintas facturas).
     *
     * fetch = FetchType.LAZY: el Producto se carga solo bajo demanda.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    /**
     * Relación N:1 con Factura: muchos Detalles pertenecen a una misma
     * Factura. Este es el lado PROPIETARIO de la relación con Factura
     * (contiene la columna FK "factura_id"), complementario al
     * @OneToMany(mappedBy = "factura") definido en la clase Factura.
     *
     * fetch = FetchType.LAZY: la Factura no se carga automáticamente al
     * traer un Detalle, salvo que se acceda explícitamente.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "factura_id", nullable = false)
    private Factura factura;
}
