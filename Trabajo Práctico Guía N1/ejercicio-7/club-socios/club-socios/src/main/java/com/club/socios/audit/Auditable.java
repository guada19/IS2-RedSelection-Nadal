package com.club.socios.audit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * ============================================================================
 * AUDITORÍA DE ENTIDADES (requisito del enunciado)
 * ============================================================================
 * Clase base que NO se mapea como tabla propia (@MappedSuperclass): sus
 * campos se agregan como columnas extra a cada entidad que la extienda
 * (Socio, FamiliarSocio, GrupoFamiliar, RegistroAcceso, Cuota, Pago...).
 *
 * @EntityListeners(AuditingEntityListener.class) es el "gancho" de JPA que,
 * en cada evento de persistencia (prePersist / preUpdate), delega en Spring
 * Data JPA Auditing el completado automático de estos 4 campos:
 *
 *   - fechaCreacion   -> @CreatedDate     (se completa 1 sola vez, al insertar)
 *   - fechaModificacion -> @LastModifiedDate (se actualiza en cada UPDATE)
 *   - creadoPor       -> @CreatedBy       (usuario logueado que creó el registro)
 *   - modificadoPor   -> @LastModifiedBy  (usuario logueado que lo modificó)
 *
 * El "quién" (creadoPor / modificadoPor) se resuelve consultando el
 * {@link AuditorAwareImpl}, que a su vez lee el usuario autenticado desde el
 * SecurityContext de Spring Security.
 * ============================================================================
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {

    @CreatedDate
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @LastModifiedDate
    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    @CreatedBy
    @Column(name = "creado_por", updatable = false, length = 100)
    private String creadoPor;

    @LastModifiedBy
    @Column(name = "modificado_por", length = 100)
    private String modificadoPor;

    /**
     * Campo de BAJA LÓGICA (soft delete). No es parte de la auditoría de
     * fechas/usuario pero se ubica aquí porque es un atributo transversal
     * a todas las entidades del dominio: en vez de borrar físicamente un
     * socio o un familiar (lo que rompería el historial de accesos y pagos),
     * se lo marca como inactivo.
     */
    @JsonIgnore
    @Column(name = "activo", nullable = false)
    private boolean activo = true;
}
