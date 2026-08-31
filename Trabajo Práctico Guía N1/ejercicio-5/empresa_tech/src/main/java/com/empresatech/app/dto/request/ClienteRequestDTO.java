package com.empresatech.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de ENTRADA (Request) para crear/actualizar un Cliente.
 *
 * -----------------------------------------------------------------------
 * Por qué se separan los DTOs en "Request" y "Response"
 * -----------------------------------------------------------------------
 * Un mismo recurso (Cliente) tiene necesidades muy distintas según la
 * dirección del dato:
 *
 *  - Lo que el usuario ENVÍA (Request) nunca debería incluir campos como
 *    "id" (lo asigna la base de datos) ni "eliminado" (es un detalle de
 *    gestión interna, no algo que el usuario decida al cargar un
 *    cliente). Además, es el único lugar donde tienen sentido las
 *    anotaciones de Jakarta Validation: solo se valida lo que llega
 *    "de afuera hacia adentro".
 *
 *  - Lo que el sistema DEVUELVE (Response) sí necesita el "id" (para que
 *    la vista pueda armar links de editar/eliminar) y puede incluir
 *    datos derivados/formateados que no tiene sentido validar porque el
 *    usuario nunca los escribe a mano.
 *
 * Usar un único DTO para ambos casos obligaría a tener campos con
 * anotaciones contradictorias (el "id" a veces obligatorio, a veces
 * prohibido) o a ignorar validaciones según el contexto, lo cual es
 * frágil y confuso. Separar Request/Response hace que cada clase tenga
 * un único propósito y sea válida en el 100% de los casos en que se usa.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    // @NotBlank: rechaza null, cadena vacía y cadenas de solo espacios.
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100, message = "El apellido no puede superar los 100 caracteres")
    private String apellido;

    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "\\d{7,10}", message = "El DNI debe contener entre 7 y 10 dígitos numéricos")
    // @Pattern: valida el formato exacto mediante una expresión regular,
    // evitando que lleguen letras o longitudes inválidas antes de tocar
    // la base de datos.
    private String dni;

    @Pattern(regexp = "^$|\\+?[0-9\\-\\s]{6,30}", message = "El teléfono tiene un formato inválido")
    // Campo opcional: el patrón admite explícitamente la cadena vacía
    // ("^$") además del formato de teléfono válido.
    private String telefono;

    // No se incluye "id": en un alta no existe todavía, y en una
    // actualización se recibe por la URL/path del endpoint (PUT /clientes/{id}),
    // no dentro del cuerpo del request, para evitar que el cliente pueda
    // "pisar" el id de otro registro enviando un valor distinto por error.

    // No se incluye "eliminado": es un estado que se gestiona a través de
    // una operación explícita de baja (DELETE /clientes/{id}), nunca
    // como parte de un alta o edición común.
}
