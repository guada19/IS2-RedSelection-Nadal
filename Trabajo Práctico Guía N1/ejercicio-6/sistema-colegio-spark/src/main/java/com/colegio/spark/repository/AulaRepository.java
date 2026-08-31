package com.colegio.spark.repository;

import com.colegio.spark.model.Aula;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/** Repositorio JPA para la entidad Aula. */
public interface AulaRepository extends JpaRepository<Aula, Long> {

    /** Todas las aulas que pertenecen a un grado determinado. */
    List<Aula> findByGradoId(Long gradoId);

}
