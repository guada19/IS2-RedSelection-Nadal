package com.colegio.spark.service;

import com.colegio.spark.dto.request.AlumnoDTO;
import com.colegio.spark.dto.response.AlumnoResponseDTO;

import java.util.List;

/** Contrato de la capa Service para las operaciones CRUD de Alumno. */
public interface AlumnoService {

    AlumnoResponseDTO crear(AlumnoDTO dto);

    List<AlumnoResponseDTO> listarTodos();

    AlumnoResponseDTO buscarPorId(Long id);

    AlumnoResponseDTO actualizar(Long id, AlumnoDTO dto);

    void eliminar(Long id);

    /** Alumnos de un aula puntual (usado por el docente al elegir a quien cargarle nota). */
    List<AlumnoResponseDTO> listarPorAula(Long aulaId);

}
