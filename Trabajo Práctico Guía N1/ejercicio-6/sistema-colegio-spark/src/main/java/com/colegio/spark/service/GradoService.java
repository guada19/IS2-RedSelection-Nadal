package com.colegio.spark.service;

import com.colegio.spark.dto.request.GradoDTO;
import com.colegio.spark.dto.response.GradoResponseDTO;

import java.util.List;

/** Contrato de la capa Service para las operaciones CRUD de Grado. */
public interface GradoService {

    GradoResponseDTO crear(GradoDTO dto);

    List<GradoResponseDTO> listarTodos();

    GradoResponseDTO buscarPorId(Long id);

    GradoResponseDTO actualizar(Long id, GradoDTO dto);

    void eliminar(Long id);

}
