package com.colegio.spark.model;

import com.colegio.spark.model.base.Auditable;
import jakarta.persistence.*;
import lombok.*;

/**
 * ============================================================================
 *  ENTIDAD: Materia             (capa Model / Persistencia - ORM)
 * ============================================================================
 * Representa una asignatura/materia dictada en el colegio (ej: "Matematica",
 * "Lengua y Literatura", "Educacion Fisica").
 *
 * Relaciones:
 *   - 1 Materia <--> N AsignacionDocente   (mappedBy en AsignacionDocente.materia)
 *   - 1 Materia <--> N Nota                (mappedBy en Nota.materia)
 * ============================================================================
 */
@Entity
@Table(name = "materias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Materia extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 80)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

}
