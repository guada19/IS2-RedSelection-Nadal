package com.colegio.spark.service.impl;

import com.colegio.spark.dto.request.AsignacionDocenteDTO;
import com.colegio.spark.dto.response.AsignacionDocenteResponseDTO;
import com.colegio.spark.exception.RecursoNoEncontradoException;
import com.colegio.spark.mapper.AsignacionDocenteMapper;
import com.colegio.spark.model.AsignacionDocente;
import com.colegio.spark.model.Aula;
import com.colegio.spark.model.Docente;
import com.colegio.spark.model.Materia;
import com.colegio.spark.repository.AsignacionDocenteRepository;
import com.colegio.spark.repository.AulaRepository;
import com.colegio.spark.repository.DocenteRepository;
import com.colegio.spark.repository.MateriaRepository;
import com.colegio.spark.service.AsignacionDocenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementacion de la logica de negocio para AsignacionDocente. Resuelve
 * Docente, Materia y Aula a partir de los id recibidos, y valida que no se
 * cree una asignacion duplicada (misma terna docente-materia-aula).
 */
@Service
@RequiredArgsConstructor
public class AsignacionDocenteServiceImpl implements AsignacionDocenteService {

    private final AsignacionDocenteRepository asignacionRepository;
    private final DocenteRepository docenteRepository;
    private final MateriaRepository materiaRepository;
    private final AulaRepository aulaRepository;
    private final AsignacionDocenteMapper asignacionMapper;

    @Override
    @Transactional
    public AsignacionDocenteResponseDTO crear(AsignacionDocenteDTO dto) {
        if (asignacionRepository.existsByDocenteIdAndMateriaIdAndAulaId(
                dto.getDocenteId(), dto.getMateriaId(), dto.getAulaId())) {
            throw new IllegalArgumentException(
                    "Esa asignacion (docente, materia y aula) ya existe");
        }

        Docente docente = docenteRepository.findById(dto.getDocenteId())
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un docente con id: " + dto.getDocenteId()));
        Materia materia = materiaRepository.findById(dto.getMateriaId())
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una materia con id: " + dto.getMateriaId()));
        Aula aula = aulaRepository.findById(dto.getAulaId())
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un aula con id: " + dto.getAulaId()));

        AsignacionDocente asignacion = AsignacionDocente.builder()
                .docente(docente)
                .materia(materia)
                .aula(aula)
                .build();

        return asignacionMapper.toResponseDTO(asignacionRepository.save(asignacion));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsignacionDocenteResponseDTO> listarTodas() {
        return asignacionRepository.findAll().stream().map(asignacionMapper::toResponseDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsignacionDocenteResponseDTO> listarPorDocente(Long docenteId) {
        return asignacionRepository.findByDocenteId(docenteId).stream()
                .map(asignacionMapper::toResponseDTO).toList();
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        AsignacionDocente asignacion = asignacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una asignacion con id: " + id));
        asignacionRepository.delete(asignacion);
    }

}
