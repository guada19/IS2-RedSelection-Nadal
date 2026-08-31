package com.colegio.spark.service.impl;

import com.colegio.spark.dto.request.GradoDTO;
import com.colegio.spark.dto.response.GradoResponseDTO;
import com.colegio.spark.exception.RecursoNoEncontradoException;
import com.colegio.spark.mapper.GradoMapper;
import com.colegio.spark.model.Grado;
import com.colegio.spark.repository.GradoRepository;
import com.colegio.spark.service.GradoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** Implementacion de la logica de negocio CRUD para Grado. */
@Service
@RequiredArgsConstructor
public class GradoServiceImpl implements GradoService {

    private final GradoRepository gradoRepository;
    private final GradoMapper gradoMapper;

    @Override
    @Transactional
    public GradoResponseDTO crear(GradoDTO dto) {
        if (gradoRepository.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new IllegalArgumentException("Ya existe un grado con el nombre '" + dto.getNombre() + "'");
        }
        Grado guardado = gradoRepository.save(gradoMapper.toEntity(dto));
        return gradoMapper.toResponseDTO(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GradoResponseDTO> listarTodos() {
        return gradoRepository.findAll().stream().map(gradoMapper::toResponseDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public GradoResponseDTO buscarPorId(Long id) {
        return gradoMapper.toResponseDTO(obtenerOrLanzar(id));
    }

    @Override
    @Transactional
    public GradoResponseDTO actualizar(Long id, GradoDTO dto) {
        Grado grado = obtenerOrLanzar(id);
        gradoMapper.actualizarEntity(grado, dto);
        return gradoMapper.toResponseDTO(grado);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Grado grado = obtenerOrLanzar(id);
        gradoRepository.delete(grado);
    }

    private Grado obtenerOrLanzar(Long id) {
        return gradoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un grado con id: " + id));
    }

}
