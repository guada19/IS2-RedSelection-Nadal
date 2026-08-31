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
 * Subclase de Factura que representa una COMPRA a un Proveedor.
 *
 * Al igual que FacturaCliente, comparte la tabla física "facturas"
 * (herencia SINGLE_TABLE) y se distingue mediante la columna
 * discriminadora "tipo_factura" con el valor "PROVEEDOR".
 */
@Entity
@DiscriminatorValue("PROVEEDOR")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class FacturaProveedor extends Factura {

    /**
     * Relación N:1 con Proveedor: muchas facturas de compra pueden
     * corresponder a un mismo Proveedor.
     *
     * fetch = FetchType.LAZY: el Proveedor se carga solo bajo demanda.
     *
     * @JoinColumn define la columna FK "proveedor_id", que convive en la
     * misma tabla "facturas" junto a "cliente_id" (quedando NULL en las
     * filas que correspondan a FacturaCliente).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;
}
