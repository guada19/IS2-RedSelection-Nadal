package com.colegio.spark.repository;

import com.colegio.spark.model.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/** Repositorio JPA para la entidad Alumno. */
public interface AlumnoRepository extends JpaRepository<Alumno, Long> {

    Optional<Alumno> findByDni(String dni);

    boolean existsByDni(String dni);

    /** Alumnos inscriptos en un aula determinada (usado por el docente al cargar notas). */
    List<Alumno> findByAulaId(Long aulaId);

    List<Alumno> findByGradoId(Long gradoId);

}
