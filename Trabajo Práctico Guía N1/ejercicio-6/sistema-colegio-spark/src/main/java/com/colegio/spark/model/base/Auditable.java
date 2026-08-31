package com.colegio.spark.model.base;

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
 *  Auditable - Superclase de AUDITORIA DE ENTIDADES (capa Model)
 * ============================================================================
 * Todas las entidades del dominio (Docente, Alumno, Grado, Aula, Materia,
 * AsignacionDocente, Nota) EXTIENDEN esta clase para heredar, sin repetir
 * codigo, cuatro columnas de auditoria:
 *
 *   - fechaCreacion     : fecha/hora en que se creo el registro
 *   - fechaModificacion : fecha/hora de la ultima modificacion
 *   - creadoPor         : usuario (email del docente logueado) que creo el registro
 *   - modificadoPor     : usuario que realizo la ultima modificacion
 *
 * ANOTACIONES CLAVE:
 *
 *  @MappedSuperclass
 *      Le indica a JPA/Hibernate que esta clase NO es una entidad ni tiene
 *      tabla propia, pero que sus atributos deben "mapearse" (agregarse como
 *      columnas) dentro de la tabla de cada subclase concreta (Docente,
 *      Alumno, etc.). Es el mecanismo estandar de JPA para reutilizar
 *      columnas comunes entre varias entidades.
 *
 *  @EntityListeners(AuditingEntityListener.class)
 *      Registra un "listener" de Spring Data JPA que intercepta los eventos
 *      de persistencia (antes de INSERT / antes de UPDATE) y completa
 *      automaticamente los campos anotados con @CreatedDate, @LastModifiedDate,
 *      @CreatedBy y @LastModifiedBy. Para que estas anotaciones funcionen es
 *      necesario habilitar la auditoria JPA a nivel de aplicacion con
 *      @EnableJpaAuditing (ver config/JpaAuditingConfig.java) y proveer un
 *      bean AuditorAware<String> (ver config/AuditorAwareImpl.java) que le
 *      diga al framework "quien es el usuario actual" (se obtiene del
 *      contexto de seguridad de Spring Security).
 *
 *  @CreatedDate / @LastModifiedDate
 *      Spring Data JPA completa automaticamente estas fechas usando el reloj
 *      del servidor, sin necesidad de asignarlas manualmente en los Services.
 *
 *  @CreatedBy / @LastModifiedBy
 *      Spring Data JPA completa automaticamente estos campos con el valor
 *      devuelto por el AuditorAware (en este proyecto: el email del docente
 *      autenticado, o "sistema" si la operacion no tiene un usuario asociado,
 *      por ejemplo el auto-registro de un docente nuevo).
 *
 *  @JsonIgnore
 *      Evita que estos campos de auditoria se filtren "sin querer" si alguna
 *      entidad llegara a serializarse a JSON (defensa en profundidad; en este
 *      proyecto la comunicacion entre capas usa DTO, por lo que las entidades
 *      nunca deberian exponerse directamente, pero se deja la anotacion como
 *      buena practica).
 * ============================================================================
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {

    @CreatedDate
    @Column(name = "fecha_creacion", updatable = false)
    @JsonIgnore
    private LocalDateTime fechaCreacion;

    @LastModifiedDate
    @Column(name = "fecha_modificacion")
    @JsonIgnore
    private LocalDateTime fechaModificacion;

    @CreatedBy
    @Column(name = "creado_por", updatable = false, length = 150)
    @JsonIgnore
    private String creadoPor;

    @LastModifiedBy
    @Column(name = "modificado_por", length = 150)
    @JsonIgnore
    private String modificadoPor;

}
