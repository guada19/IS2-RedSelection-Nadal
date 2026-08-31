package com.colegio.spark.model;

import com.colegio.spark.model.base.Auditable;
import jakarta.persistence.*;
import lombok.*;

/**
 * ============================================================================
 *  ENTIDAD: Grado              (capa Model / Persistencia - ORM)
 * ============================================================================
 * Representa un grado/año escolar (ej: "1er Grado", "6to Grado", "3er Año").
 * Un Grado agrupa Alumnos y puede tener una o mas Aulas asociadas
 * (por ejemplo "5to Grado - Aula A" y "5to Grado - Aula B").
 *
 * Relaciones:
 *   - 1 Grado  <-->  N Aulas    (mappedBy en Aula.grado)
 *   - 1 Grado  <-->  N Alumnos  (mappedBy en Alumno.grado)
 * ============================================================================
 */
@Entity
@Table(name = "grados")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Grado extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Ej: "1er Grado", "2do Grado", "3er Año Secundaria" */
    @Column(nullable = false, unique = true, length = 60)
    private String nombre;

    /** Ej: "Primario" / "Secundario" - nivel educativo al que pertenece */
    @Column(nullable = false, length = 40)
    private String nivel;

}
