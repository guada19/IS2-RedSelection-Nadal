package com.empresatech.app.repository;

import com.empresatech.app.model.Detalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleRepository extends JpaRepository<Detalle, Long> {

    /**
     * Recupera todas las líneas de detalle pertenecientes a una factura
     * puntual (navega la relación Detalle -> Factura -> id).
     */
    List<Detalle> findByFacturaId(Long facturaId);
}
