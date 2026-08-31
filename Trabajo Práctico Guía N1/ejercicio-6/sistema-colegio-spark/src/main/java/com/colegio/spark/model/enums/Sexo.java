package com.colegio.spark.model.enums;

/**
 * Enum que representa el sexo de una persona (docente o alumno).
 * Se persiste en la base de datos como texto (ver @Enumerated(EnumType.STRING)
 * en las entidades que lo usan) para que el valor guardado en la columna sea
 * legible ("MASCULINO"/"FEMENINO") en lugar de un numero ordinal, evitando
 * errores si en el futuro se reordenan los valores del enum.
 */
public enum Sexo {
    MASCULINO,
    FEMENINO
}
