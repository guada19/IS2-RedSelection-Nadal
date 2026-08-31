package com.empresatech.app.repository;

import com.empresatech.app.model.EstadoFactura;
import com.empresatech.app.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio genérico sobre la entidad abstracta Factura.
 *
 * Gracias a la herencia SINGLE_TABLE, este repositorio permite consultar
 * de forma POLIMÓRFICA tanto FacturaCliente como FacturaProveedor a la
 * vez (por ejemplo, findAll() trae ambos tipos mezclados), ya que ambas
 * subclases comparten la misma tabla física "facturas".
 */
@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {

    List<Factura> findByEstado(EstadoFactura estado);

    List<Factura> findByEliminadoFalse();

    Optional<Factura> findByNroFactura(String nroFactura);
}
