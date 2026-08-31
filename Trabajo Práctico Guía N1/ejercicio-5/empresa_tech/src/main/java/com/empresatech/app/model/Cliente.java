package com.empresatech.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa a un Cliente de la tienda, es decir, la persona
 * a la que se le emite una FacturaCliente al vender un producto.
 */
@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 100)
    private String apellido;

    @Column(name = "dni", nullable = false, unique = true, length = 15)
    // unique = true: a nivel de base de datos no pueden existir dos
    // clientes con el mismo DNI (documento de identidad).
    private String dni;

    @Column(name = "telefono", length = 30)
    private String telefono;

    @Column(name = "eliminado", nullable = false)
    // Borrado lógico: permite "dar de baja" a un cliente sin perder el
    // historial de facturas ya emitidas a su nombre.
    @Builder.Default
    private boolean eliminado = false;
}
