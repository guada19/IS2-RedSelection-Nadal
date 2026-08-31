package com.colegio.spark.repository;

import com.colegio.spark.model.AsignacionDocente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/** Repositorio JPA para la entidad AsignacionDocente (docente + materia + aula). */
public interface AsignacionDocenteRepository extends JpaRepository<AsignacionDocente, Long> {

    /** Todas las asignaciones (materia + aula) que tiene a cargo un docente. */
    List<AsignacionDocente> findByDocenteId(Long docenteId);

    boolean existsByDocenteIdAndMateriaIdAndAulaId(Long docenteId, Long materiaId, Long aulaId);

}
