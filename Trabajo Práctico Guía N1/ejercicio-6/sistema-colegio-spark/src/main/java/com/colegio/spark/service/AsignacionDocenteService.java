package com.colegio.spark.service;

import com.colegio.spark.dto.request.AsignacionDocenteDTO;
import com.colegio.spark.dto.response.AsignacionDocenteResponseDTO;

import java.util.List;

/**
 * Contrato de la capa Service para gestionar que docente dicta que materia
 * en que aula.
 */
public interface AsignacionDocenteService {

    AsignacionDocenteResponseDTO crear(AsignacionDocenteDTO dto);

    List<AsignacionDocenteResponseDTO> listarTodas();

    /** Asignaciones (materia + aula) que tiene a cargo un docente puntual. */
    List<AsignacionDocenteResponseDTO> listarPorDocente(Long docenteId);

    void eliminar(Long id);

}
