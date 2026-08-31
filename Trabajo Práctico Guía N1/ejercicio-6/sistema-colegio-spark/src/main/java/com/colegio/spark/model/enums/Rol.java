package com.colegio.spark.model.enums;

/**
 * Roles de seguridad reconocidos por el sistema.
 *
 *  - ADMIN   : gestiona la estructura academica del colegio (grados, aulas,
 *              materias, alumnos, asignacion de docentes a materias/aulas) y
 *              administra las cuentas de los docentes.
 *  - DOCENTE : rol con el que se registra un profesor. Puede iniciar sesion,
 *              ver/editar su perfil, cambiar su contraseña y cargar notas de
 *              los alumnos en las materias/aulas que tiene asignadas.
 *
 * Este enum se traduce a un "GrantedAuthority" de Spring Security con el
 * prefijo "ROLE_" (por convencion del framework) en CustomUserDetailsService,
 * por ejemplo Rol.ADMIN -> "ROLE_ADMIN". Las reglas de autorizacion por URL se
 * definen en SecurityConfig usando esos mismos nombres.
 */
public enum Rol {
    ADMIN,
    DOCENTE
}
