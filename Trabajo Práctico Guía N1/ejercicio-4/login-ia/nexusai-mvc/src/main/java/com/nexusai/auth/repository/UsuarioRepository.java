package com.nexusai.auth.repository;

import com.nexusai.auth.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * ============================================================================
 * REPOSITORIO — UsuarioRepository (CAPA DE ACCESO A DATOS / ORM)
 * ============================================================================
 * Esta interfaz forma parte de la capa de persistencia. NO se implementa a
 * mano: Spring Data JPA genera automáticamente, en tiempo de ejecución (vía
 * proxy dinámico), una implementación concreta que traduce cada método a
 * sentencias SQL ejecutadas por Hibernate contra MySQL.
 *
 * ANOTACIONES / HERENCIA:
 *
 *  @Repository            Marca la interfaz como un "Bean" de la capa de
 *                          persistencia y habilita la traducción automática
 *                          de excepciones específicas de MySQL/JDBC hacia la
 *                          jerarquía unificada DataAccessException de Spring.
 *                          (Es opcional en interfaces JpaRepository porque
 *                          Spring Data ya las detecta, pero se deja explícita
 *                          por claridad didáctica).
 *
 *  extends JpaRepository<Usuario, Long>
 *                          Al heredar de JpaRepository, la interfaz obtiene
 *                          "gratis" el CRUD completo sobre la entidad Usuario
 *                          (cuya clave primaria es de tipo Long):
 *                            save(), findById(), findAll(), deleteById(),
 *                            count(), existsById(), etc.
 *
 * MÉTODOS DE CONSULTA DERIVADOS ("Query Methods"):
 *   Spring Data JPA es capaz de generar la consulta SQL a partir del NOMBRE
 *   del método, siguiendo una convención (findBy + NombreDeAtributo). No
 *   hace falta escribir SQL ni JPQL para estos casos simples.
 * ============================================================================
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca un usuario por su correo personal (que actúa como "username").
     * Traducido por Spring Data a:
     *   SELECT u FROM Usuario u WHERE u.correoPersonal = :correoPersonal
     * Se usa tanto en el login (para validar credenciales) como en el
     * registro (para verificar que el correo no esté ya usado).
     */
    Optional<Usuario> findByCorreoPersonal(String correoPersonal);

    /** Verifica existencia de un correo sin traer la entidad completa (más eficiente). */
    boolean existsByCorreoPersonal(String correoPersonal);

    /** Verifica existencia de un documento ya registrado. */
    boolean existsByDocumento(String documento);
}
