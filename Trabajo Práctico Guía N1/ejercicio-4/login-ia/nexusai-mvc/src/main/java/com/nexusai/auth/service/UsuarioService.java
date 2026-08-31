package com.nexusai.auth.service;

import com.nexusai.auth.dto.RegistroUsuarioDTO;
import com.nexusai.auth.model.Usuario;

/**
 * ============================================================================
 * INTERFAZ DE SERVICIO — UsuarioService (CAPA DE LÓGICA DE NEGOCIO)
 * ============================================================================
 * En una arquitectura MVC "estricta" el Controller NO debe contener reglas
 * de negocio: su única responsabilidad es (a) recibir la petición HTTP,
 * (b) delegar en el Service, y (c) elegir qué VISTA devolver según el
 * resultado. Toda la lógica de negocio (validar duplicados, encriptar
 * claves, contar intentos fallidos, bloquear la cuenta) vive acá, en la
 * capa Service.
 *
 * Se programa contra una INTERFAZ (y no directamente contra la clase de
 * implementación) siguiendo el principio de inversión de dependencias:
 * el Controller depende de esta abstracción, no de un detalle concreto.
 * Esto facilita además el testing (se puede inyectar un mock en pruebas
 * unitarias del Controller).
 * ============================================================================
 */
public interface UsuarioService {

    /**
     * Registra un nuevo usuario en el sistema.
     * Reglas de negocio aplicadas dentro de la implementación:
     *   - El correo personal y el documento deben ser únicos.
     *   - Las claves ingresadas (clave / claveConfirmacion) deben coincidir.
     *   - La clave se persiste encriptada (BCrypt), nunca en texto plano.
     *
     * @param dto datos completos del formulario de registro
     * @return la entidad Usuario ya persistida (con su id generado)
     */
    Usuario registrar(RegistroUsuarioDTO dto);

    /**
     * Intenta autenticar a un usuario contra el correo y la clave provistos.
     * Implementa la regla central del enunciado: "si el usuario no está
     * registrado se le solicita registrarse" y "si equivoca la clave 3
     * veces el mismo se bloquea".
     *
     * @param correoPersonal correo ingresado (actúa como nombre de usuario)
     * @param claveIngresada clave en texto plano tal como la tipeó el usuario
     * @return la entidad Usuario autenticada correctamente
     *
     * @throws com.nexusai.auth.exception.UsuarioNoRegistradoException
     *         si no existe ningún usuario con ese correo.
     * @throws com.nexusai.auth.exception.UsuarioBloqueadoException
     *         si la cuenta está bloqueada (ya sea de antes, o porque este
     *         intento fallido fue justamente el que la bloqueó).
     * @throws com.nexusai.auth.exception.CredencialesInvalidasException
     *         si la clave es incorrecta pero todavía quedan intentos.
     */
    Usuario login(String correoPersonal, String claveIngresada);
}
