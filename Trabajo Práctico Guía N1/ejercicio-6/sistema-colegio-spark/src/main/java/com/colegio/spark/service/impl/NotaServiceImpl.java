package com.colegio.spark.service.impl;

import com.colegio.spark.dto.request.NotaDTO;
import com.colegio.spark.dto.response.NotaResponseDTO;
import com.colegio.spark.exception.RecursoNoEncontradoException;
import com.colegio.spark.mapper.NotaMapper;
import com.colegio.spark.model.Alumno;
import com.colegio.spark.model.Docente;
import com.colegio.spark.model.Materia;
import com.colegio.spark.model.Nota;
import com.colegio.spark.repository.AlumnoRepository;
import com.colegio.spark.repository.AsignacionDocenteRepository;
import com.colegio.spark.repository.DocenteRepository;
import com.colegio.spark.repository.MateriaRepository;
import com.colegio.spark.repository.NotaRepository;
import com.colegio.spark.service.NotaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * ============================================================================
 *  NotaServiceImpl               (capa Service - LOGICA DE NEGOCIO)
 * ============================================================================
 * Implementa la carga y consulta de calificaciones. La regla de negocio mas
 * importante de esta clase es de AUTORIZACION A NIVEL DE DATOS (mas fina que
 * la autorizacion por URL de SecurityConfig): un docente solo puede cargar
 * una nota en una Materia+Aula para las que EXISTE una AsignacionDocente a su
 * nombre. Esto evita que, por ejemplo, el profesor de Matematica de 5to
 * Grado pudiera cargar notas de Educacion Fisica en 3er Grado con solo
 * conocer los id de alumno/materia.
 * ============================================================================
 */
@Service
@RequiredArgsConstructor
public class NotaServiceImpl implements NotaService {

    private final NotaRepository notaRepository;
    private final AlumnoRepository alumnoRepository;
    private final MateriaRepository materiaRepository;
    private final DocenteRepository docenteRepository;
    private final AsignacionDocenteRepository asignacionDocenteRepository;
    private final NotaMapper notaMapper;

    @Override
    @Transactional
    public NotaResponseDTO crear(NotaDTO dto, String emailDocenteAutenticado) {
        Docente docente = obtenerDocentePorEmail(emailDocenteAutenticado);
        Alumno alumno = obtenerAlumnoOrLanzar(dto.getAlumnoId());
        Materia materia = obtenerMateriaOrLanzar(dto.getMateriaId());

        validarDocenteTieneAsignacion(docente.getId(), materia.getId(), alumno.getAula().getId());

        Nota nota = Nota.builder()
                .alumno(alumno)
                .materia(materia)
                .docente(docente)
                .valor(dto.getValor())
                .periodo(dto.getPeriodo())
                .fechaEvaluacion(dto.getFechaEvaluacion())
                .observaciones(dto.getObservaciones())
                .build();

        return notaMapper.toResponseDTO(notaRepository.save(nota));
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotaResponseDTO> listarPorDocente(String emailDocenteAutenticado) {
        Docente docente = obtenerDocentePorEmail(emailDocenteAutenticado);
        return notaRepository.findByDocenteId(docente.getId()).stream()
                .map(notaMapper::toResponseDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotaResponseDTO> listarTodas() {
        return notaRepository.findAll().stream().map(notaMapper::toResponseDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public NotaResponseDTO buscarPorId(Long id) {
        return notaMapper.toResponseDTO(obtenerNotaOrLanzar(id));
    }

    @Override
    @Transactional
    public NotaResponseDTO actualizar(Long id, NotaDTO dto, String emailDocenteAutenticado) {
        Nota nota = obtenerNotaOrLanzar(id);
        Docente docente = obtenerDocentePorEmail(emailDocenteAutenticado);

        // Un docente solo puede editar las notas que el mismo cargo (el rol ADMIN
        // se filtra a nivel de URL en SecurityConfig, pero la carga/edicion de una
        // nota puntual siempre queda asociada a quien la genero originalmente).
        validarPertenenciaDeLaNota(nota, docente);

        Alumno alumno = obtenerAlumnoOrLanzar(dto.getAlumnoId());
        Materia materia = obtenerMateriaOrLanzar(dto.getMateriaId());
        validarDocenteTieneAsignacion(docente.getId(), materia.getId(), alumno.getAula().getId());

        nota.setAlumno(alumno);
        nota.setMateria(materia);
        nota.setValor(dto.getValor());
        nota.setPeriodo(dto.getPeriodo());
        nota.setFechaEvaluacion(dto.getFechaEvaluacion());
        nota.setObservaciones(dto.getObservaciones());

        return notaMapper.toResponseDTO(nota);
    }

    @Override
    @Transactional
    public void eliminar(Long id, String emailDocenteAutenticado) {
        Nota nota = obtenerNotaOrLanzar(id);
        Docente docente = obtenerDocentePorEmail(emailDocenteAutenticado);
        validarPertenenciaDeLaNota(nota, docente);
        notaRepository.delete(nota);
    }

    // ------------------------------------------------------------------
    // Metodos privados de apoyo (validaciones y busquedas reutilizadas)
    // ------------------------------------------------------------------

    private void validarDocenteTieneAsignacion(Long docenteId, Long materiaId, Long aulaId) {
        boolean tieneAsignacion = asignacionDocenteRepository
                .existsByDocenteIdAndMateriaIdAndAulaId(docenteId, materiaId, aulaId);
        if (!tieneAsignacion) {
            throw new IllegalStateException(
                    "El docente no tiene asignada esa materia en el aula del alumno seleccionado");
        }
    }

    private void validarPertenenciaDeLaNota(Nota nota, Docente docente) {
        boolean esAdmin = docente.getRol().name().equals("ADMIN");
        boolean esElDuenio = nota.getDocente().getId().equals(docente.getId());
        if (!esAdmin && !esElDuenio) {
            throw new IllegalStateException("No tiene permisos para modificar esta nota");
        }
    }

    private Nota obtenerNotaOrLanzar(Long id) {
        return notaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una nota con id: " + id));
    }

    private Alumno obtenerAlumnoOrLanzar(Long id) {
        return alumnoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un alumno con id: " + id));
    }

    private Materia obtenerMateriaOrLanzar(Long id) {
        return materiaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una materia con id: " + id));
    }

    private Docente obtenerDocentePorEmail(String email) {
        return docenteRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un docente con el correo: " + email));
    }

}
