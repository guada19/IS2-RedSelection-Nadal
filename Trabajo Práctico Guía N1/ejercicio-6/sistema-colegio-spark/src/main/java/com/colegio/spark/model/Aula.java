package com.colegio.spark.model;

import com.colegio.spark.model.base.Auditable;
import jakarta.persistence.*;
import lombok.*;

/**
 * ============================================================================
 *  ENTIDAD: Aula                (capa Model / Persistencia - ORM)
 * ============================================================================
 * Representa un aula fisica del colegio (ej: "Aula A", "Laboratorio 1").
 * Cada Aula pertenece a un unico Grado (relacion @ManyToOne: muchas aulas
 * pueden pertenecer al mismo grado, por ejemplo dos divisiones de 5to Grado).
 *
 * Relaciones:
 *   - N Aulas  --> 1 Grado                 (@ManyToOne, columna grado_id)
 *   - 1 Aula   <--> N Alumnos               (mappedBy en Alumno.aula)
 *   - 1 Aula   <--> N AsignacionDocente     (mappedBy en AsignacionDocente.aula)
 * ============================================================================
 */
@Entity
@Table(name = "aulas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "grado") // evita recursion/consultas extra al loguear
public class Aula extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Ej: "Aula A", "Aula 12", "Pabellon B - Sala 3" */
    @Column(nullable = false, length = 60)
    private String nombre;

    @Column(nullable = false)
    private Integer capacidad;

    /**
     * Grado al que pertenece esta aula.
     * fetch = LAZY -> el Grado NO se carga automaticamente junto con el Aula;
     * se consulta a la base de datos recien cuando se invoca getGrado()
     * (buena practica de rendimiento con JPA, evita cargar datos de mas).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grado_id", nullable = false)
    private Grado grado;

}
