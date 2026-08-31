package com.colegio.spark.model;

import com.colegio.spark.model.base.Auditable;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * ============================================================================
 *  ENTIDAD: Alumno              (capa Model / Persistencia - ORM)
 * ============================================================================
 * Representa a un estudiante del colegio. Cada Alumno esta inscripto en un
 * Grado y en un Aula concretos (ambas relaciones @ManyToOne: muchos alumnos
 * pertenecen al mismo grado/aula).
 *
 * Relaciones:
 *   - N Alumnos --> 1 Grado    (@ManyToOne, columna grado_id)
 *   - N Alumnos --> 1 Aula     (@ManyToOne, columna aula_id)
 *   - 1 Alumno  <--> N Notas   (mappedBy en Nota.alumno) -> notas por materia
 * ============================================================================
 */
@Entity
@Table(name = "alumnos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"grado", "aula"})
public class Alumno extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String nombre;

    @Column(nullable = false, length = 80)
    private String apellido;

    /** Documento Nacional de Identidad, unico por alumno */
    @Column(nullable = false, unique = true, length = 15)
    private String dni;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grado_id", nullable = false)
    private Grado grado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aula_id", nullable = false)
    private Aula aula;

}
