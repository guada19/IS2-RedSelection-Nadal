package com.colegio.spark.service;

import com.colegio.spark.dto.request.AulaDTO;
import com.colegio.spark.dto.response.AulaResponseDTO;

import java.util.List;

/** Contrato de la capa Service para las operaciones CRUD de Aula. */
public interface AulaService {

    AulaResponseDTO crear(AulaDTO dto);

    List<AulaResponseDTO> listarTodas();

    AulaResponseDTO buscarPorId(Long id);

    AulaResponseDTO actualizar(Long id, AulaDTO dto);

    void eliminar(Long id);

}
