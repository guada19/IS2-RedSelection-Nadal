package com.colegio.spark.repository;

import com.colegio.spark.model.Nota;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/** Repositorio JPA para la entidad Nota. */
public interface NotaRepository extends JpaRepository<Nota, Long> {

    /** Notas cargadas por un docente determinado (para que solo vea/edite las propias). */
    List<Nota> findByDocenteId(Long docenteId);

    /** Historial de notas de un alumno, para armar su boletin. */
    List<Nota> findByAlumnoIdOrderByFechaEvaluacionDesc(Long alumnoId);

    /** Notas de una materia dictada en un aula puntual por un docente puntual. */
    List<Nota> findByDocenteIdAndMateriaIdAndAlumno_AulaId(Long docenteId, Long materiaId, Long aulaId);

}
