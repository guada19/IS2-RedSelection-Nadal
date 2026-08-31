package com.empresatech.app.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Subclase de Factura que representa una VENTA a un Cliente.
 *
 * Al usar herencia SINGLE_TABLE, esta entidad no genera una tabla propia:
 * sus filas se guardan en la tabla "facturas" (heredada de Factura), y
 * Hibernate las distingue mediante la columna discriminadora
 * "tipo_factura" (que tomará automáticamente el valor "FacturaCliente").
 */
@Entity
// @Entity: obligatoria en cada subclase concreta de la jerarquía para
// que Hibernate la reconozca como un tipo persistible independiente
// (con su propio valor de discriminador).
@DiscriminatorValue("CLIENTE")
// @DiscriminatorValue: define el valor concreto que se guardará en la
// columna "tipo_factura" (definida en la clase padre) para identificar
// las filas que corresponden a esta subclase. Si se omite, Hibernate usa
// por defecto el nombre de la clase.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class FacturaCliente extends Factura {

    /**
     * Relación N:1 con Cliente: muchas facturas de venta pueden
     * corresponder a un mismo Cliente.
     *
     * fetch = FetchType.LAZY: el Cliente no se carga automáticamente al
     * traer la factura, solo al acceder explícitamente a
     * facturaCliente.getCliente().
     *
     * @JoinColumn define la columna FK "cliente_id" dentro de la propia
     * tabla "facturas" (recordar que es SINGLE_TABLE). Esta columna
     * queda en NULL para las filas que en realidad son FacturaProveedor.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
}
