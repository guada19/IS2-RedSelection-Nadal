package com.colegio.spark.model;

import com.colegio.spark.model.base.Auditable;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * ============================================================================
 *  ENTIDAD: Nota                (capa Model / Persistencia - ORM)
 * ============================================================================
 * Representa la calificacion de UN Alumno en UNA Materia, cargada por el
 * Docente correspondiente, dentro de un periodo lectivo (ej: "1er Trimestre").
 *
 * Relaciones:
 *   - N Notas --> 1 Alumno   (a quien pertenece la calificacion)
 *   - N Notas --> 1 Materia  (en que asignatura)
 *   - N Notas --> 1 Docente  (quien la cargo, trazabilidad academica ademas
 *                             de la auditoria tecnica heredada de Auditable)
 * ============================================================================
 */
@Entity
@Table(name = "notas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"alumno", "materia", "docente"})
public class Nota extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alumno_id", nullable = false)
    private Alumno alumno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "materia_id", nullable = false)
    private Materia materia;

    /** Docente que cargo la calificacion (trazabilidad academica) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "docente_id", nullable = false)
    private Docente docente;

    /** Calificacion numerica, en la escala 1.00 a 10.00 */
    @Column(nullable = false, precision = 4, scale = 2)
    private BigDecimal valor;

    /** Ej: "1er Trimestre", "2do Trimestre", "Final" */
    @Column(nullable = false, length = 40)
    private String periodo;

    @Column(name = "fecha_evaluacion", nullable = false)
    private LocalDate fechaEvaluacion;

    @Column(length = 255)
    private String observaciones;

}
