package com.colegio.spark.service;

import com.colegio.spark.dto.request.MateriaDTO;
import com.colegio.spark.dto.response.MateriaResponseDTO;

import java.util.List;

/** Contrato de la capa Service para las operaciones CRUD de Materia. */
public interface MateriaService {

    MateriaResponseDTO crear(MateriaDTO dto);

    List<MateriaResponseDTO> listarTodas();

    MateriaResponseDTO buscarPorId(Long id);

    MateriaResponseDTO actualizar(Long id, MateriaDTO dto);

    void eliminar(Long id);

}
