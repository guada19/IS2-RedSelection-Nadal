package com.colegio.spark.repository;

import com.colegio.spark.model.Grado;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA para la entidad Grado. Ver DocenteRepository para el
 * detalle de como Spring Data JPA genera automaticamente la implementacion.
 */
public interface GradoRepository extends JpaRepository<Grado, Long> {

    boolean existsByNombreIgnoreCase(String nombre);

}
