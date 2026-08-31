package com.nexusai.auth.service.impl;

import com.nexusai.auth.dto.RegistroUsuarioDTO;
import com.nexusai.auth.exception.CredencialesInvalidasException;
import com.nexusai.auth.exception.DatoYaRegistradoException;
import com.nexusai.auth.exception.UsuarioBloqueadoException;
import com.nexusai.auth.exception.UsuarioNoRegistradoException;
import com.nexusai.auth.model.Usuario;
import com.nexusai.auth.repository.UsuarioRepository;
import com.nexusai.auth.service.UsuarioService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ============================================================================
 * IMPLEMENTACIÓN DEL SERVICIO — UsuarioServiceImpl (CAPA DE NEGOCIO)
 * ============================================================================
 * ANOTACIONES:
 *
 *  @Service        Estereotipo de Spring que marca la clase como un Bean de
 *                  la capa de servicio. Es semánticamente una especialización
 *                  de @Component; el contenedor IoC la detecta durante el
 *                  @ComponentScan y la instancia una única vez (singleton),
 *                  quedando disponible para ser inyectada en el Controller.
 *
 *  @Transactional  Envuelve cada método público en una transacción de base
 *                  de datos: si ocurre una excepción en el medio del método
 *                  (por ejemplo, al guardar el intento fallido), Spring hace
 *                  ROLLBACK automático de todos los cambios realizados hasta
 *                  ese punto, garantizando que el estado de la BD quede
 *                  consistente (todo o nada).
 *
 * INYECCIÓN DE DEPENDENCIAS:
 *   Las dependencias (UsuarioRepository y PasswordEncoder) se reciben por
 *   CONSTRUCTOR en vez de usar @Autowired sobre atributos. Es la forma
 *   recomendada actualmente por el equipo de Spring: hace que las
 *   dependencias sean explícitas, inmutables (final) y fáciles de testear.
 * ============================================================================
 */
@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Cantidad máxima de intentos fallidos permitidos antes de bloquear la
     * cuenta. Se inyecta desde application.properties con @Value, evitando
     * un "número mágico" hardcodeado en la lógica de negocio.
     */
    @Value("${seguridad.intentos-maximos:3}")
    private int intentosMaximos;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Usuario registrar(RegistroUsuarioDTO dto) {
        // --- Validaciones de negocio adicionales a las de Bean Validation ---
        if (usuarioRepository.existsByCorreoPersonal(dto.getCorreoPersonal())) {
            throw new DatoYaRegistradoException("Ya existe un usuario registrado con ese correo personal.");
        }
        if (usuarioRepository.existsByDocumento(dto.getDocumento())) {
            throw new DatoYaRegistradoException("Ya existe un usuario registrado con ese documento.");
        }
        if (!dto.lasClavesCoinciden()) {
            throw new DatoYaRegistradoException("Las claves ingresadas no coinciden.");
        }

        // --- Mapeo DTO (capa Vista/Controller) -> Entidad (capa Modelo/ORM) ---
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setDocumento(dto.getDocumento());
        usuario.setFechaNacimiento(dto.getFechaNacimiento());
        usuario.setCorreoPersonal(dto.getCorreoPersonal());
        // La clave NUNCA se guarda en texto plano: se aplica hash BCrypt.
        usuario.setClave(passwordEncoder.encode(dto.getClave()));
        usuario.setIntentosFallidos(0);
        usuario.setBloqueado(false);

        // save() delegó en Hibernate: genera el INSERT y completa el id autogenerado.
        return usuarioRepository.save(usuario);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(noRollbackFor = {CredencialesInvalidasException.class, UsuarioBloqueadoException.class})
    public Usuario login(String correoPersonal, String claveIngresada) {

        // 1) ¿Existe el usuario? -> Regla: "si no está registrado, se le
        //    solicita registrarse".
        Usuario usuario = usuarioRepository.findByCorreoPersonal(correoPersonal)
                .orElseThrow(() -> new UsuarioNoRegistradoException(
                        "El correo ingresado no está registrado. Por favor, regístrese."));

        // 2) ¿La cuenta ya está bloqueada de antes? Se corta el flujo antes
        //    de siquiera comparar la clave.
        if (Boolean.TRUE.equals(usuario.getBloqueado())) {
            throw new UsuarioBloqueadoException(
                    "Su cuenta se encuentra bloqueada por exceder los " + intentosMaximos +
                    " intentos de clave incorrecta. Contacte al administrador.");
        }

        // 3) Comparación segura: passwordEncoder.matches() aplica el mismo
        //    algoritmo BCrypt sobre "claveIngresada" y compara contra el
        //    hash almacenado (nunca se desencripta la clave guardada).
        boolean claveCorrecta = passwordEncoder.matches(claveIngresada, usuario.getClave());

        if (claveCorrecta) {
            // Login exitoso: se reinicia el contador de intentos fallidos.
            usuario.setIntentosFallidos(0);
            usuarioRepository.save(usuario);
            return usuario;
        }

        // 4) Clave incorrecta: se incrementa el contador de intentos.
        int intentosPrevios = usuario.getIntentosFallidos() == null ? 0 : usuario.getIntentosFallidos();
        int nuevosIntentos = intentosPrevios + 1;
        usuario.setIntentosFallidos(nuevosIntentos);

        if (nuevosIntentos >= intentosMaximos) {
            // Se alcanzó el máximo de intentos: se bloquea la cuenta.
            usuario.setBloqueado(true);
            usuarioRepository.save(usuario);
            throw new UsuarioBloqueadoException(
                    "Ha superado los " + intentosMaximos + " intentos permitidos. Su cuenta fue bloqueada.");
        }

        usuarioRepository.save(usuario);
        int intentosRestantes = intentosMaximos - nuevosIntentos;
        throw new CredencialesInvalidasException(
                "La clave ingresada es incorrecta.", intentosRestantes);
    }
}
