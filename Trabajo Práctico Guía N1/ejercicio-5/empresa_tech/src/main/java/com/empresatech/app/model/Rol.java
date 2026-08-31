package com.empresatech.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa un Rol de seguridad (por ejemplo: "ADMIN",
 * "VENDEDOR", "DEPOSITO"), utilizado por Spring Security para determinar
 * qué acciones puede realizar cada Usuario dentro del sistema.
 */
@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
// @EqualsAndHashCode(of = "id"): genera equals()/hashCode() basados
// únicamente en el campo "id". Es importante restringirlo así (y no
// generar equals/hashCode con todos los campos, ni usar el de Object)
// porque Rol participa de una relación @ManyToMany dentro de un Set en
// Usuario, y Hibernate necesita una comparación estable y basada en la
// identidad de la entidad para que ese Set funcione correctamente.
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true, length = 50)
    // Nombre del rol, por ejemplo "ROLE_ADMIN". Se recomienda el prefijo
    // "ROLE_" por convención de Spring Security al usar hasRole(...).
    private String nombre;
}
