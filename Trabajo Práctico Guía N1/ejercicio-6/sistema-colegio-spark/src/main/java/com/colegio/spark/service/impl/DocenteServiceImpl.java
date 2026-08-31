package com.colegio.spark.service.impl;

import com.colegio.spark.dto.request.CambioPasswordDTO;
import com.colegio.spark.dto.request.DocenteEdicionDTO;
import com.colegio.spark.dto.request.DocenteRegistroDTO;
import com.colegio.spark.dto.response.DocenteResponseDTO;
import com.colegio.spark.exception.EmailYaRegistradoException;
import com.colegio.spark.exception.PasswordActualIncorrectaException;
import com.colegio.spark.exception.RecursoNoEncontradoException;
import com.colegio.spark.mapper.DocenteMapper;
import com.colegio.spark.model.Docente;
import com.colegio.spark.model.enums.Rol;
import com.colegio.spark.repository.DocenteRepository;
import com.colegio.spark.service.DocenteService;
import com.colegio.spark.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * ============================================================================
 *  DocenteServiceImpl            (capa Service - LOGICA DE NEGOCIO)
 * ============================================================================
 * Implementa las reglas de negocio de Docente. Es la capa intermedia entre
 * el Controller (que solo conoce DTO y delega aqui) y el Repository (acceso
 * a datos). Aca es donde:
 *   - se validan reglas que exceden a Bean Validation (ej: email unico,
 *     contraseñas coincidentes, contraseña actual correcta),
 *   - se hashea la contraseña con BCrypt antes de persistir,
 *   - se dispara el envio del correo de bienvenida,
 *   - se convierten Entidad <-> DTO usando el DocenteMapper.
 *
 *  @Service   -> estereotipo de Spring para la capa de logica de negocio.
 *  @Transactional -> cada metodo publico se ejecuta dentro de una transaccion
 *      JPA: si algo falla a mitad de camino, todos los cambios se revierten
 *      (ACID). Tambien es indispensable para que las relaciones LAZY de las
 *      entidades (ej: Aula.grado) puedan navegarse dentro del metodo sin
 *      lanzar LazyInitializationException.
 * ============================================================================
 */
@Service
@RequiredArgsConstructor
public class DocenteServiceImpl implements DocenteService {

    private final DocenteRepository docenteRepository;
    private final DocenteMapper docenteMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    @Transactional
    public DocenteResponseDTO registrarDocente(DocenteRegistroDTO dto) {
        if (!dto.getPassword().equals(dto.getConfirmarPassword())) {
            throw new IllegalArgumentException("Las contraseñas ingresadas no coinciden");
        }
        if (docenteRepository.existsByEmail(dto.getEmail())) {
            throw new EmailYaRegistradoException(
                    "Ya existe un docente registrado con el correo " + dto.getEmail());
        }

        Docente docente = Docente.builder()
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .sexo(dto.getSexo())
                .fechaNacimiento(dto.getFechaNacimiento())
                .email(dto.getEmail())
                // La contraseña NUNCA se guarda en texto plano: se aplica BCrypt.
                .password(passwordEncoder.encode(dto.getPassword()))
                // Todo auto-registro publico recibe el rol DOCENTE; el rol ADMIN
                // se reserva para asignacion manual (ver README.md).
                .rol(Rol.DOCENTE)
                .activo(true)
                .build();

        Docente guardado = docenteRepository.save(docente);

        // Envio del correo de bienvenida al correo personal del docente (asincrono,
        // no bloquea ni hace fallar el registro si el servidor de correo falla).
        emailService.enviarCorreoBienvenida(guardado);

        return docenteMapper.toResponseDTO(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocenteResponseDTO> listarTodos() {
        return docenteRepository.findAll().stream()
                .map(docenteMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DocenteResponseDTO buscarPorId(Long id) {
        return docenteMapper.toResponseDTO(obtenerDocenteOrLanzar(id));
    }

    @Override
    @Transactional(readOnly = true)
    public DocenteResponseDTO buscarPorEmail(String email) {
        Docente docente = docenteRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un docente con el correo: " + email));
        return docenteMapper.toResponseDTO(docente);
    }

    @Override
    @Transactional
    public DocenteResponseDTO actualizarDatosPersonales(Long id, DocenteEdicionDTO dto) {
        Docente docente = obtenerDocenteOrLanzar(id);
        docente.setNombre(dto.getNombre());
        docente.setApellido(dto.getApellido());
        docente.setSexo(dto.getSexo());
        docente.setFechaNacimiento(dto.getFechaNacimiento());
        return docenteMapper.toResponseDTO(docente);
        // No hace falta llamar a docenteRepository.save(): al estar dentro de una
        // transaccion @Transactional y haber sido "docente" obtenido desde el
        // repositorio (entidad "managed"), Hibernate detecta los cambios sobre sus
        // atributos y genera el UPDATE automaticamente al finalizar la transaccion
        // (mecanismo de "dirty checking" propio del ORM).
    }

    @Override
    @Transactional
    public void cambiarPassword(String email, CambioPasswordDTO dto) {
        Docente docente = docenteRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un docente con el correo: " + email));

        if (!passwordEncoder.matches(dto.getPasswordActual(), docente.getPassword())) {
            throw new PasswordActualIncorrectaException("La contraseña actual ingresada es incorrecta");
        }
        if (!dto.getPasswordNueva().equals(dto.getConfirmarPasswordNueva())) {
            throw new IllegalArgumentException("La nueva contraseña y su confirmacion no coinciden");
        }

        docente.setPassword(passwordEncoder.encode(dto.getPasswordNueva()));
    }

    @Override
    @Transactional
    public void cambiarEstadoActivo(Long id, boolean activo) {
        Docente docente = obtenerDocenteOrLanzar(id);
        docente.setActivo(activo);
    }

    /** Metodo privado de apoyo: evita repetir el findById+orElseThrow en cada operacion. */
    private Docente obtenerDocenteOrLanzar(Long id) {
        return docenteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un docente con id: " + id));
    }

}
