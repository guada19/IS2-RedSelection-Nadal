package com.colegio.spark.service;

import com.colegio.spark.dto.request.NotaDTO;
import com.colegio.spark.dto.response.NotaResponseDTO;

import java.util.List;

/**
 * Contrato de la capa Service para la carga y consulta de notas.
 * El email del docente autenticado se recibe como parametro (no viaja en el
 * DTO) para que sea siempre el propio Service quien determine, contra la
 * sesion real, quien esta cargando la nota.
 */
public interface NotaService {

    /** Carga una nueva nota, validando que el docente tenga esa materia/aula asignada. */
    NotaResponseDTO crear(NotaDTO dto, String emailDocenteAutenticado);

    /** Notas cargadas por el docente autenticado (para ADMIN, ver listarTodas()). */
    List<NotaResponseDTO> listarPorDocente(String emailDocenteAutenticado);

    List<NotaResponseDTO> listarTodas();

    NotaResponseDTO buscarPorId(Long id);

    NotaResponseDTO actualizar(Long id, NotaDTO dto, String emailDocenteAutenticado);

    void eliminar(Long id, String emailDocenteAutenticado);

}
