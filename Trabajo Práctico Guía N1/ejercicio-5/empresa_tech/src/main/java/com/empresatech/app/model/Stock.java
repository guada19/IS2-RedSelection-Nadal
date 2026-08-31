package com.empresatech.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entidad que representa el inventario (existencias) de un Producto.
 *
 * Se separa de Producto en una tabla propia porque la cantidad en stock
 * cambia con mucha más frecuencia que los datos descriptivos del
 * producto (nombre, precio), y porque conceptualmente son
 * responsabilidades distintas dentro del dominio (catálogo vs. inventario).
 */
@Entity
@Table(name = "stock")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cantidad", nullable = false)
    // Cantidad de unidades disponibles del producto asociado.
    private Integer cantidad;

    @Column(name = "actualizacion", nullable = false)
    // Fecha y hora de la última actualización del stock (por ejemplo,
    // tras una venta o una reposición). Se usa LocalDateTime en vez de
    // java.util.Date por ser la API moderna de fechas de Java (java.time),
    // inmutable y mucho más segura para manipular fechas/horas.
    private LocalDateTime actualizacion;

    /**
     * Relación 1:1 con Producto. Este es el lado PROPIETARIO de la
     * relación: la tabla "stock" contendrá físicamente la columna
     * "producto_id" como clave foránea (FK) hacia "productos".
     *
     * @JoinColumn especifica el nombre de esa columna FK.
     *
     * fetch = FetchType.LAZY: el Producto asociado no se carga
     * automáticamente al consultar un Stock; se carga solo cuando se
     * invoca explícitamente stock.getProducto().
     *
     * unique = true en la @JoinColumn refuerza a nivel de base de datos
     * que la relación es efectivamente 1:1 (un producto_id no puede
     * repetirse en más de una fila de stock).
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false, unique = true)
    private Producto producto;
}
