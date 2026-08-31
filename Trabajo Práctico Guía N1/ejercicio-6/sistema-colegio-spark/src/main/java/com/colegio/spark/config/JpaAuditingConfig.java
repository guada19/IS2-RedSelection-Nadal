package com.colegio.spark.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * ============================================================================
 *  JpaAuditingConfig            (capa Config - AUDITORIA DE ENTIDADES)
 * ============================================================================
 * Habilita a nivel de toda la aplicacion el mecanismo de auditoria automatica
 * de Spring Data JPA.
 *
 *  @EnableJpaAuditing
 *      Activa el "motor" que procesa las anotaciones @CreatedDate,
 *      @LastModifiedDate, @CreatedBy y @LastModifiedBy declaradas en
 *      model/base/Auditable.java. Sin esta anotacion, esos campos NUNCA se
 *      completarian automaticamente aunque la entidad tenga
 *      @EntityListeners(AuditingEntityListener.class).
 *
 *  auditorAwareRef = "auditorAwareImpl"
 *      Le indica a Spring Data JPA el nombre del bean (ver AuditorAwareImpl)
 *      que debe consultar para saber "quien es el usuario actual" y asi
 *      completar los campos creadoPor / modificadoPor.
 * ============================================================================
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl")
public class JpaAuditingConfig {

    /**
     * Bean requerido por @EnableJpaAuditing: le dice a Spring Data JPA cual
     * es la implementacion concreta de AuditorAware<String> a utilizar.
     * La logica real (leer el usuario autenticado desde el
     * SecurityContextHolder) esta en la clase AuditorAwareImpl.
     */
    @Bean
    public AuditorAware<String> auditorAwareImpl() {
        return new AuditorAwareImpl();
    }

}
