package com.colegio.spark.exception;

/**
 * Excepcion de negocio lanzada cuando se busca por id una entidad (Docente,
 * Alumno, Grado, Aula, Materia, AsignacionDocente, Nota) que no existe en la
 * base de datos. Es una RuntimeException (unchecked) para no obligar a los
 * Services/Controllers a declarar "throws" en cada firma de metodo.
 * La captura GlobalExceptionHandler para convertirla en una vista 404.
 */
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }

}
