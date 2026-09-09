package com.club.socios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * ============================================================================
 * PUNTO DE ENTRADA DE LA APLICACIÓN
 * ============================================================================
 * Levanta el contenedor Spring (Tomcat embebido) y arranca todos los
 * componentes anotados con @Controller, @Service, @Repository, etc.
 *
 * @EnableJpaAuditing habilita la AUDITORÍA DE ENTIDADES: permite que los
 * campos anotados con @CreatedDate, @LastModifiedDate, @CreatedBy y
 * @LastModifiedBy (ver clase base {@link com.club.socios.audit.Auditable})
 * se completen automáticamente en cada INSERT/UPDATE, usando el
 * {@link com.club.socios.audit.AuditorAwareImpl} para saber "quién" hizo
 * el cambio (usuario autenticado en Spring Security).
 * ============================================================================
 */
@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class ClubSociosApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClubSociosApplication.class, args);
    }
}
