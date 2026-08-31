package com.colegio.spark.repository;

import com.colegio.spark.model.Docente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * ============================================================================
 *  DocenteRepository            (capa Repository - ORM / Acceso a datos)
 * ============================================================================
 * Interfaz de acceso a datos para la entidad Docente. Al extender
 * JpaRepository<Docente, Long>, Spring Data JPA genera automaticamente, en
 * tiempo de ejecucion (mediante un proxy dinamico), la implementacion de:
 *   - Operaciones CRUD basicas: save(), findById(), findAll(), deleteById()...
 *   - Paginacion y ordenamiento (Pageable, Sort).
 *
 * Ademas, se declaran metodos de consulta "derivados" (query methods): Spring
 * Data JPA interpreta el nombre del metodo y genera automaticamente la
 * consulta SQL/HQL correspondiente, sin necesidad de escribir una sola linea
 * de SQL.
 * ============================================================================
 */
public interface DocenteRepository extends JpaRepository<Docente, Long> {

    /** Busca un docente por su email (usado como "username" de login). */
    Optional<Docente> findByEmail(String email);

    /** Verifica si ya existe un docente registrado con ese email (evita duplicados) */
    boolean existsByEmail(String email);

}
