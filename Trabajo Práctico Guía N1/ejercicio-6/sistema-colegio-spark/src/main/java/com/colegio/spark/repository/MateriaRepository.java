package com.colegio.spark.repository;

import com.colegio.spark.model.Materia;
import org.springframework.data.jpa.repository.JpaRepository;

/** Repositorio JPA para la entidad Materia. */
public interface MateriaRepository extends JpaRepository<Materia, Long> {

    boolean existsByNombreIgnoreCase(String nombre);

}
