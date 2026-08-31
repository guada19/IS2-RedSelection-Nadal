package com.colegio.spark.service.impl;

import com.colegio.spark.dto.request.MateriaDTO;
import com.colegio.spark.dto.response.MateriaResponseDTO;
import com.colegio.spark.exception.RecursoNoEncontradoException;
import com.colegio.spark.mapper.MateriaMapper;
import com.colegio.spark.model.Materia;
import com.colegio.spark.repository.MateriaRepository;
import com.colegio.spark.service.MateriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** Implementacion de la logica de negocio CRUD para Materia. */
@Service
@RequiredArgsConstructor
public class MateriaServiceImpl implements MateriaService {

    private final MateriaRepository materiaRepository;
    private final MateriaMapper materiaMapper;

    @Override
    @Transactional
    public MateriaResponseDTO crear(MateriaDTO dto) {
        if (materiaRepository.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new IllegalArgumentException("Ya existe una materia con el nombre '" + dto.getNombre() + "'");
        }
        Materia guardada = materiaRepository.save(materiaMapper.toEntity(dto));
        return materiaMapper.toResponseDTO(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MateriaResponseDTO> listarTodas() {
        return materiaRepository.findAll().stream().map(materiaMapper::toResponseDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MateriaResponseDTO buscarPorId(Long id) {
        return materiaMapper.toResponseDTO(obtenerOrLanzar(id));
    }

    @Override
    @Transactional
    public MateriaResponseDTO actualizar(Long id, MateriaDTO dto) {
        Materia materia = obtenerOrLanzar(id);
        materiaMapper.actualizarEntity(materia, dto);
        return materiaMapper.toResponseDTO(materia);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        materiaRepository.delete(obtenerOrLanzar(id));
    }

    private Materia obtenerOrLanzar(Long id) {
        return materiaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una materia con id: " + id));
    }

}
