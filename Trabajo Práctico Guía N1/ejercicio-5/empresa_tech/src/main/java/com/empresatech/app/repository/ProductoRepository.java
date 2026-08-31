package com.empresatech.app.repository;

import com.empresatech.app.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio de acceso a datos para la entidad Producto.
 *
 * Al extender JpaRepository<Producto, Long> se obtienen automáticamente,
 * sin escribir ninguna implementación, operaciones CRUD completas:
 * save(), findById(), findAll(), deleteById(), count(), etc. Spring Data
 * JPA genera la implementación en tiempo de ejecución (proxy dinámico).
 */
@Repository
// @Repository: marca la interfaz como un componente de la capa de
// persistencia. Habilita la traducción automática de excepciones nativas
// de JDBC/Hibernate a la jerarquía de excepciones de Spring
// (DataAccessException), además de permitir que Spring la detecte
// durante el escaneo de componentes.
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    /**
     * Método de consulta derivado: Spring Data JPA genera automáticamente
     * la consulta SQL a partir del NOMBRE del método, sin necesidad de
     * escribir @Query. "findByEliminadoFalse" se traduce a:
     * SELECT * FROM productos WHERE eliminado = false
     * Útil para listar únicamente los productos activos (no eliminados
     * lógicamente).
     */
    List<Producto> findByEliminadoFalse();
}
