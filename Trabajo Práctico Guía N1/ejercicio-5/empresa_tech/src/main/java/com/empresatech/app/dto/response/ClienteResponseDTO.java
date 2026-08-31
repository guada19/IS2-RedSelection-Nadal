package com.empresatech.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de SALIDA (Response) para mostrar un Cliente en la vista Thymeleaf
 * o devolverlo desde un endpoint REST.
 *
 * -----------------------------------------------------------------------
 * Por qué NO se usa la entidad `Cliente` directamente en la vista
 * -----------------------------------------------------------------------
 * 1) LAZY LOADING: aunque Cliente no tiene relaciones LAZY propias, sí
 *    es referenciado desde FacturaCliente (`@ManyToOne` LAZY). Si en
 *    algún punto la vista recorriera `facturaCliente.getCliente()`
 *    fuera de una sesión de Hibernate activa (recordar que este
 *    proyecto usa `spring.jpa.open-in-view=false`), se produciría un
 *    `LazyInitializationException`. Al pasar siempre por un Response DTO
 *    ya "resuelto" en la capa de servicio (dentro de una transacción
 *    activa), la vista nunca toca un proxy de Hibernate, solo datos
 *    planos ya materializados.
 *
 * 2) DESACOPLE: si mañana se agrega un campo interno a `Cliente` (por
 *    ejemplo, una relación con un historial de crédito), ese cambio no
 *    debería filtrarse automáticamente a la vista. El Response DTO actúa
 *    como un "contrato" estable entre el backend y la vista/API,
 *    independiente de cómo evolucione el modelo de persistencia.
 *
 * 3) NO SE VALIDA: a diferencia del Request DTO, este objeto viaja del
 *    servidor hacia afuera, por lo que no lleva anotaciones de Jakarta
 *    Validation (no tiene sentido "validar" un dato que el propio
 *    sistema generó y en el que ya se confía).
 *
 * Se incluye "eliminado" porque, a diferencia del Request (donde no
 * tiene sentido que el usuario lo envíe), en la vista sí puede ser útil
 * para, por ejemplo, mostrar una etiqueta "Cliente dado de baja" o para
 * decidir si mostrar un botón de "reactivar" en lugar de "eliminar".
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteResponseDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private String dni;
    private String telefono;
    private boolean eliminado;
}
