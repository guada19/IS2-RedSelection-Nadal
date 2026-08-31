package com.empresatech.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Entidad que representa un Usuario del sistema (quien inicia sesión
 * para operar la tienda: cargar ventas, gestionar stock, etc.).
 *
 * Esta entidad está pensada para integrarse con Spring Security: sus
 * campos "username", "password" y "enabled" corresponden directamente
 * a los que espera la interfaz UserDetails de Spring Security al
 * construir un adaptador/implementación propia (por ejemplo, una clase
 * UsuarioDetailsService que traduzca este Usuario a un UserDetails).
 */
@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    // Nombre de usuario utilizado para iniciar sesión. unique=true evita
    // usuarios duplicados.
    private String username;

    @Column(name = "password", nullable = false, length = 100)
    // Contraseña del usuario. IMPORTANTE: nunca se guarda en texto plano;
    // se almacena ya cifrada con BCrypt (mediante un
    // BCryptPasswordEncoder configurado en la capa de seguridad), un
    // algoritmo de hash unidireccional con "salt" incorporado, diseñado
    // específicamente para contraseñas.
    private String password;

    @Column(name = "enabled", nullable = false)
    // Indica si la cuenta está habilitada para iniciar sesión. Spring
    // Security consulta este campo (a través de UserDetails.isEnabled())
    // para permitir o bloquear el login sin necesidad de borrar al
    // usuario.
    @Builder.Default
    private boolean enabled = true;

    /**
     * Relación N:M con Rol: un Usuario puede tener varios Roles, y un
     * mismo Rol puede pertenecer a varios Usuarios.
     *
     * fetch = FetchType.EAGER: a diferencia de las demás relaciones de
     * este proyecto (LAZY por defecto), aquí se elige carga inmediata
     * porque los roles del usuario se necesitan sí o sí en cada request
     * autenticado para resolver sus permisos, y son una colección
     * pequeña, por lo que el costo adicional es mínimo.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    // @JoinTable: como es una relación N:M, Hibernate necesita una tabla
    // intermedia (aquí "usuarios_roles") con dos columnas FK: una hacia
    // "usuarios" (joinColumns) y otra hacia "roles" (inverseJoinColumns).
    @JoinTable(
            name = "usuarios_roles",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    @Builder.Default
    private Set<Rol> roles = new HashSet<>();
    // Se usa Set (no List) porque conceptualmente un usuario no debería
    // tener el mismo rol repetido, y Set lo garantiza automáticamente
    // (apoyándose en el equals/hashCode por "id" definido en Rol).
}
