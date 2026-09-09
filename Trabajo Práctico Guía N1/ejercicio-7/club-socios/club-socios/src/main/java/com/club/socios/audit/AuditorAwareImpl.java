package com.club.socios.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * ============================================================================
 * PROVEEDOR DE "AUDITOR" (usuario actual) PARA LA AUDITORÍA DE ENTIDADES
 * ============================================================================
 * Spring Data JPA invoca a getCurrentAuditor() cada vez que necesita
 * completar los campos @CreatedBy / @LastModifiedBy de {@link Auditable}.
 *
 * Se apoya en el SecurityContext de SPRING SECURITY: obtiene el "Authentication"
 * de la sesión HTTP actual y devuelve el username del usuario logueado.
 * Si no hay nadie autenticado (por ejemplo, un job batch o el arranque de
 * datos de prueba), se devuelve "sistema" como autor por defecto.
 * ============================================================================
 */
@Component("auditorProvider")
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            return Optional.of("sistema");
        }
        return Optional.of(authentication.getName());
    }
}
