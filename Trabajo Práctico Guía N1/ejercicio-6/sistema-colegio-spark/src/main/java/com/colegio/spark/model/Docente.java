package com.colegio.spark.model;

import com.colegio.spark.model.base.Auditable;
import com.colegio.spark.model.enums.Rol;
import com.colegio.spark.model.enums.Sexo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

/**
 * ============================================================================
 *  ENTIDAD: Docente            (capa Model / Persistencia - ORM)
 * ============================================================================
 * Representa a un profesor del colegio. Es, a la vez, la entidad de dominio
 * Y la entidad que provee las credenciales de acceso al sistema:
 *
 *      - "usuario" de login  -> el campo email (correo personal del docente)
 *      - "contraseña"        -> el campo password, guardado SIEMPRE con hash
 *                                BCrypt (nunca en texto plano). Ver
 *                                CustomUserDetailsService y DocenteServiceImpl.
 *
 * Un Docente puede tener 0..N asignaciones (AsignacionDocente) que indican en
 * que Materia y Aula dicta clases, y puede haber cargado 0..N Notas de los
 * alumnos.
 *
 * ANOTACIONES:
 *  @Entity                 -> marca la clase como una entidad JPA (se mapea a
 *                              una tabla de la base de datos).
 *  @Table(name=...)        -> nombre explicito de la tabla en MySQL.
 *  @Id / @GeneratedValue    -> clave primaria autoincremental (IDENTITY es la
 *                              estrategia nativa de autoincremento en MySQL).
 *  @Column                  -> ajustes finos de la columna (unique, nullable,
 *                              longitud, nombre de columna en snake_case).
 *  @Enumerated(EnumType.STRING) -> persiste el enum como texto legible.
 *  extends Auditable         -> hereda fechaCreacion/fechaModificacion/
 *                              creadoPor/modificadoPor (auditoria).
 * ============================================================================
 */
@Entity
@Table(name = "docentes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "password") // nunca exponer el hash de la contraseña en logs
public class Docente extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String nombre;

    @Column(nullable = false, length = 80)
    private String apellido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private Sexo sexo;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    /**
     * Correo personal del docente. Cumple una doble funcion:
     *  1) Dato de contacto (a donde se envia el correo de bienvenida).
     *  2) "Username" con el que el docente inicia sesion (ver
     *     CustomUserDetailsService.loadUserByUsername).
     * Debe ser unico en toda la tabla.
     */
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    /**
     * Contraseña de acceso, almacenada SIEMPRE como hash BCrypt
     * (ver PasswordEncoder en SecurityConfig). Jamas se persiste ni se
     * muestra en texto plano.
     */
    @Column(nullable = false, length = 200)
    private String password;

    /**
     * Rol de seguridad del docente dentro del sistema. Por defecto, todo
     * docente que se auto-registra recibe el rol DOCENTE (ver
     * DocenteServiceImpl.registrarDocente). El rol ADMIN se asigna
     * manualmente en la base de datos o mediante un DataInitializer.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private Rol rol;

    /**
     * Baja logica: en lugar de borrar fisicamente al docente (lo que
     * romperia la integridad referencial con las Notas y Asignaciones que
     * cargo), un administrador puede desactivar su cuenta. Un docente
     * inactivo no puede iniciar sesion (ver CustomUserDetailsService,
     * atributo "enabled" del UserDetails).
     */
    @Column(nullable = false)
    @Builder.Default
    private boolean activo = true;

}
