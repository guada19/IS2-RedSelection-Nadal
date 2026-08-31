package com.colegio.spark.model;

import com.colegio.spark.model.base.Auditable;
import jakarta.persistence.*;
import lombok.*;

/**
 * ============================================================================
 *  ENTIDAD: AsignacionDocente    (capa Model / Persistencia - ORM)
 * ============================================================================
 * Entidad "intermedia"/"asociativa" que resuelve la relacion de muchos-a-
 * muchos entre Docente, Materia y Aula: indica que UN docente dicta UNA
 * materia determinada en UN aula determinada
 * (ej: "Prof. Garcia dicta Matematica en 5to Grado - Aula A").
 *
 * Se modela como entidad propia (en lugar de una simple tabla @ManyToMany)
 * porque es informacion de negocio con entidad propia: es exactamente el dato
 * que le permite al sistema saber, cuando un docente inicia sesion, en que
 * materias/aulas puede cargar notas (ver NotaController / NotaServiceImpl).
 *
 * Restriccion de unicidad: no puede existir mas de una asignacion identica
 * (mismo docente + misma materia + misma aula), evitando duplicados.
 * ============================================================================
 */
@Entity
@Table(
        name = "asignaciones_docente",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_docente_materia_aula",
                columnNames = {"docente_id", "materia_id", "aula_id"}
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"docente", "materia", "aula"})
public class AsignacionDocente extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "docente_id", nullable = false)
    private Docente docente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "materia_id", nullable = false)
    private Materia materia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aula_id", nullable = false)
    private Aula aula;

}
