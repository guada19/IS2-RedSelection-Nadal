package com.empresatech.app.mapper;

import com.empresatech.app.dto.request.ClienteRequestDTO;
import com.empresatech.app.dto.response.ClienteResponseDTO;
import com.empresatech.app.model.Cliente;
import org.springframework.stereotype.Component;

/**
 * Mapper de la entidad Cliente <-> sus DTOs de Request/Response.
 *
 * -----------------------------------------------------------------------
 * Por qué el patrón DTO + Mapper (visión general, válida para todos los
 * mappers de este paquete)
 * -----------------------------------------------------------------------
 * El Mapper es la ÚNICA pieza del sistema que conoce simultáneamente el
 * modelo de persistencia (entidad JPA) y el modelo de transporte (DTO).
 * Ni el Controlador ni la Vista (Thymeleaf) llegan a ver una entidad JPA
 * directamente: el Controlador solo recibe/devuelve DTOs, y delega en el
 * Mapper (normalmente a través de la capa de Servicio) la traducción
 * hacia/desde el modelo de dominio. Esto trae tres beneficios concretos:
 *
 *  1) Evita `LazyInitializationException`: la conversión Entidad -> DTO
 *     ocurre DENTRO de la capa de servicio, mientras la transacción/sesión
 *     de Hibernate todavía está abierta, por lo que acceder a relaciones
 *     LAZY (como `factura.getCliente()`) en ese momento es seguro. Una
 *     vez que el DTO resultante sale de esa capa, ya no contiene ningún
 *     proxy de Hibernate, solo valores planos, así que la vista puede
 *     usarlo libremente sin errores aunque la sesión ya se haya cerrado
 *     (`spring.jpa.open-in-view=false`).
 *
 *  2) Desacopla el modelo de dominio de la vista: el Controlador y las
 *     plantillas Thymeleaf dependen de los DTOs, no de las entidades. Si
 *     el modelo de persistencia cambia (se agrega una relación, se
 *     renombra una columna), mientras el Mapper se actualice, la vista
 *     ni se entera.
 *
 *  3) Rompe ciclos de referencia: las entidades tienen relaciones
 *     bidireccionales (Producto<->Stock, Factura<->Detalle) que
 *     provocarían recursión infinita si se serializaran tal cual. Los
 *     DTOs, diseñados a propósito de forma "unidireccional" (ver los
 *     comentarios en cada DTO), no tienen ese problema.
 *
 * Cliente es una entidad sin relaciones propias, por lo que su Mapper es
 * el más simple de todos: una traducción directa campo a campo.
 */
@Component
// @Component: registra esta clase como un bean de Spring, para poder
// inyectarla (por ejemplo, con @RequiredArgsConstructor) en la capa de
// servicio mediante @Autowired/inyección por constructor, en lugar de
// instanciarla manualmente con "new" en cada lugar donde se necesite.
public class ClienteMapper {

    /**
     * Convierte el DTO de entrada (lo que el usuario envió) en una
     * entidad Cliente lista para persistir. No asigna "id" (lo genera la
     * base de datos) ni "eliminado" (se inicializa en `false` por
     * definición de la propia entidad).
     */
    public Cliente toEntity(ClienteRequestDTO dto) {
        return Cliente.builder()
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .dni(dto.getDni())
                .telefono(dto.getTelefono())
                .build();
    }

    /**
     * Convierte una entidad Cliente (ya persistida, con id asignado) en
     * su DTO de salida, listo para enviarse a la vista o a un cliente
     * REST.
     */
    public ClienteResponseDTO toResponseDTO(Cliente cliente) {
        return ClienteResponseDTO.builder()
                .id(cliente.getId())
                .nombre(cliente.getNombre())
                .apellido(cliente.getApellido())
                .dni(cliente.getDni())
                .telefono(cliente.getTelefono())
                .eliminado(cliente.isEliminado())
                .build();
    }

    /**
     * Aplica los datos de un Request DTO SOBRE una entidad ya existente,
     * pensado para el flujo de actualización (PUT/PATCH): se busca el
     * Cliente por id en la capa de servicio, y luego se le "vuelcan" los
     * nuevos valores sin perder su id ni su estado de "eliminado"
     * actual, que no forman parte del Request DTO.
     */
    public void actualizarEntidadDesdeDTO(ClienteRequestDTO dto, Cliente cliente) {
        cliente.setNombre(dto.getNombre());
        cliente.setApellido(dto.getApellido());
        cliente.setDni(dto.getDni());
        cliente.setTelefono(dto.getTelefono());
    }
}
