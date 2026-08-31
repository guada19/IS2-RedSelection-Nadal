package com.colegio.spark.service.impl;

import com.colegio.spark.dto.request.AlumnoDTO;
import com.colegio.spark.dto.response.AlumnoResponseDTO;
import com.colegio.spark.exception.RecursoNoEncontradoException;
import com.colegio.spark.mapper.AlumnoMapper;
import com.colegio.spark.model.Alumno;
import com.colegio.spark.model.Aula;
import com.colegio.spark.model.Grado;
import com.colegio.spark.repository.AlumnoRepository;
import com.colegio.spark.repository.AulaRepository;
import com.colegio.spark.repository.GradoRepository;
import com.colegio.spark.service.AlumnoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementacion de la logica de negocio CRUD para Alumno. Resuelve las
 * relaciones Grado y Aula a partir de los id recibidos en el AlumnoDTO antes
 * de persistir, y valida que el DNI no este duplicado.
 */
@Service
@RequiredArgsConstructor
public class AlumnoServiceImpl implements AlumnoService {

    private final AlumnoRepository alumnoRepository;
    private final GradoRepository gradoRepository;
    private final AulaRepository aulaRepository;
    private final AlumnoMapper alumnoMapper;

    @Override
    @Transactional
    public AlumnoResponseDTO crear(AlumnoDTO dto) {
        if (alumnoRepository.existsByDni(dto.getDni())) {
            throw new IllegalArgumentException("Ya existe un alumno registrado con el DNI " + dto.getDni());
        }
        Alumno alumno = Alumno.builder()
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .dni(dto.getDni())
                .fechaNacimiento(dto.getFechaNacimiento())
                .grado(obtenerGradoOrLanzar(dto.getGradoId()))
                .aula(obtenerAulaOrLanzar(dto.getAulaId()))
                .build();
        return alumnoMapper.toResponseDTO(alumnoRepository.save(alumno));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlumnoResponseDTO> listarTodos() {
        return alumnoRepository.findAll().stream().map(alumnoMapper::toResponseDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AlumnoResponseDTO buscarPorId(Long id) {
        return alumnoMapper.toResponseDTO(obtenerAlumnoOrLanzar(id));
    }

    @Override
    @Transactional
    public AlumnoResponseDTO actualizar(Long id, AlumnoDTO dto) {
        Alumno alumno = obtenerAlumnoOrLanzar(id);
        alumno.setNombre(dto.getNombre());
        alumno.setApellido(dto.getApellido());
        alumno.setDni(dto.getDni());
        alumno.setFechaNacimiento(dto.getFechaNacimiento());
        alumno.setGrado(obtenerGradoOrLanzar(dto.getGradoId()));
        alumno.setAula(obtenerAulaOrLanzar(dto.getAulaId()));
        return alumnoMapper.toResponseDTO(alumno);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        alumnoRepository.delete(obtenerAlumnoOrLanzar(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlumnoResponseDTO> listarPorAula(Long aulaId) {
        return alumnoRepository.findByAulaId(aulaId).stream().map(alumnoMapper::toResponseDTO).toList();
    }

    private Alumno obtenerAlumnoOrLanzar(Long id) {
        return alumnoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un alumno con id: " + id));
    }

    private Grado obtenerGradoOrLanzar(Long id) {
        return gradoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un grado con id: " + id));
    }

    private Aula obtenerAulaOrLanzar(Long id) {
        return aulaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un aula con id: " + id));
    }

}
