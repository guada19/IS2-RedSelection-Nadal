package com.colegio.spark.service.impl;

import com.colegio.spark.dto.request.AulaDTO;
import com.colegio.spark.dto.response.AulaResponseDTO;
import com.colegio.spark.exception.RecursoNoEncontradoException;
import com.colegio.spark.mapper.AulaMapper;
import com.colegio.spark.model.Aula;
import com.colegio.spark.model.Grado;
import com.colegio.spark.repository.AulaRepository;
import com.colegio.spark.repository.GradoRepository;
import com.colegio.spark.service.AulaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementacion de la logica de negocio CRUD para Aula.
 * A diferencia de Grado/Materia, aca la construccion de la entidad requiere
 * primero resolver el Grado relacionado (buscandolo por su id en
 * GradoRepository), por eso la conversion DTO -> Entidad se hace en este
 * Service en lugar de en el AulaMapper.
 */
@Service
@RequiredArgsConstructor
public class AulaServiceImpl implements AulaService {

    private final AulaRepository aulaRepository;
    private final GradoRepository gradoRepository;
    private final AulaMapper aulaMapper;

    @Override
    @Transactional
    public AulaResponseDTO crear(AulaDTO dto) {
        Grado grado = obtenerGradoOrLanzar(dto.getGradoId());
        Aula aula = Aula.builder()
                .nombre(dto.getNombre())
                .capacidad(dto.getCapacidad())
                .grado(grado)
                .build();
        return aulaMapper.toResponseDTO(aulaRepository.save(aula));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AulaResponseDTO> listarTodas() {
        return aulaRepository.findAll().stream().map(aulaMapper::toResponseDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AulaResponseDTO buscarPorId(Long id) {
        return aulaMapper.toResponseDTO(obtenerAulaOrLanzar(id));
    }

    @Override
    @Transactional
    public AulaResponseDTO actualizar(Long id, AulaDTO dto) {
        Aula aula = obtenerAulaOrLanzar(id);
        aula.setNombre(dto.getNombre());
        aula.setCapacidad(dto.getCapacidad());
        aula.setGrado(obtenerGradoOrLanzar(dto.getGradoId()));
        return aulaMapper.toResponseDTO(aula);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        aulaRepository.delete(obtenerAulaOrLanzar(id));
    }

    private Aula obtenerAulaOrLanzar(Long id) {
        return aulaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un aula con id: " + id));
    }

    private Grado obtenerGradoOrLanzar(Long id) {
        return gradoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un grado con id: " + id));
    }

}
