package com.colegio.spark.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * ============================================================================
 *  AuditorAwareImpl             (capa Config - AUDITORIA DE ENTIDADES)
 * ============================================================================
 * Implementacion del contrato AuditorAware<String> de Spring Data JPA.
 * Cada vez que se crea o modifica una entidad que hereda de Auditable, el
 * framework invoca getCurrentAuditor() para obtener el "autor" de la
 * operacion y completar automaticamente los campos creadoPor/modificadoPor.
 *
 * La implementacion consulta el SecurityContextHolder (donde Spring Security
 * guarda, por hilo de ejecucion, los datos de la sesion autenticada actual)
 * y devuelve el "username" (en este proyecto, el email del docente logueado).
 *
 * Casos sin usuario autenticado (por ejemplo, cuando un docente se AUTO-
 * REGISTRA todavia sin haber iniciado sesion, o procesos internos del
 * sistema): se devuelve el valor fijo "sistema" para dejar constancia de que
 * la operacion no fue realizada por un usuario logueado.
 * ============================================================================
 */
public class AuditorAwareImpl implements AuditorAware<String> {

    private static final String USUARIO_SISTEMA = "sistema";

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            return Optional.of(USUARIO_SISTEMA);
        }

        return Optional.of(authentication.getName());
    }

}
