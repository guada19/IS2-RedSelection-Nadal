package com.empresatech.app.repository;


import com.empresatech.app.model.FacturaCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio especializado en FacturaCliente (ventas).
 *
 * Al declarar JpaRepository<FacturaCliente, Long>, Hibernate filtra
 * automáticamente por la columna discriminadora "tipo_factura = CLIENTE"
 * en cada consulta, devolviendo únicamente las filas de la tabla
 * "facturas" que correspondan a ventas.
 */
@Repository
public interface FacturaClienteRepository extends JpaRepository<FacturaCliente, Long> {

    List<FacturaCliente> findByClienteId(Long clienteId);
}
