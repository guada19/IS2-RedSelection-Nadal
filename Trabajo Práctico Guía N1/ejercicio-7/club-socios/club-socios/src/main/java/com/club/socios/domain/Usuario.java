package com.club.socios.domain;

import com.club.socios.audit.Auditable;
import com.club.socios.domain.enums.RolUsuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * USUARIO DEL SISTEMA (seguridad) ---> RELACIÓN UML: ASOCIACIÓN (con Persona)
 * ============================================================================
 * Cuenta de acceso a la aplicación (login) para el personal del club
 * (administración/recepción). Se modela deliberadamente INDEPENDIENTE de
 * la jerarquía Persona/Socio/FamiliarSocio: un Usuario es un concepto de
 * SEGURIDAD (credenciales + rol), mientras que Persona es un concepto de
 * DOMINIO (socio/familiar del club). Ambos mundos se enlazan con una
 * asociación opcional y débil (un empleado del club puede, además, ser
 * socio del club).
 * ============================================================================
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "usuario")
public class Usuario extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 60)
    private String username;

    /** Hash BCrypt de la contraseña (nunca se guarda en texto plano). */
    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 120)
    private String nombreCompleto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RolUsuario rol;

    /** ASOCIACIÓN opcional: a qué Persona (socio) corresponde este usuario, si aplica. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "persona_id")
    private Persona persona;
}
