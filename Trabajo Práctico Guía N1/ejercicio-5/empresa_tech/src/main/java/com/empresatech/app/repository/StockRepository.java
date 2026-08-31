package com.empresatech.app.repository;

import com.empresatech.app.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    /**
     * Busca el Stock asociado a un Producto concreto, navegando la
     * relación mediante "producto.id" (nombre del campo "producto" +
     * su campo "id"). Spring Data JPA traduce esto a un JOIN implícito.
     */
    Optional<Stock> findByProductoId(Long productoId);
}
