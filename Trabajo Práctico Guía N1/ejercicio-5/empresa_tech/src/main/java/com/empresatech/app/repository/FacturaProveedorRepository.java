package com.empresatech.app.repository;

import com.empresatech.app.model.FacturaProveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio especializado en FacturaProveedor (compras). Igual que
 * FacturaClienteRepository, pero filtrando por
 * "tipo_factura = PROVEEDOR" de forma automática.
 */
@Repository
public interface FacturaProveedorRepository extends JpaRepository<FacturaProveedor, Long> {

    List<FacturaProveedor> findByProveedorId(Long proveedorId);
}
