package com.empresatech.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa a un Proveedor, es decir, la empresa o persona
 * a la que la tienda le compra mercadería (asociada a FacturaProveedor).
 */
@Entity
@Table(name = "proveedores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cuit", nullable = false, unique = true, length = 15)
    // CUIT: Clave Única de Identificación Tributaria (identificador fiscal
    // argentino). unique=true evita proveedores duplicados.
    private String cuit;

    @Column(name = "razon_social", nullable = false, length = 150)
    // Nombre legal/comercial de la empresa proveedora.
    private String razonSocial;

    @Column(name = "telefono", length = 30)
    private String telefono;

    @Column(name = "activo", nullable = false)
    // Indica si el proveedor está actualmente habilitado para operar con
    // la tienda (a diferencia de "eliminado", se modela como "activo"
    // porque conceptualmente refleja si la relación comercial sigue vigente).
    @Builder.Default
    private boolean activo = true;
}
