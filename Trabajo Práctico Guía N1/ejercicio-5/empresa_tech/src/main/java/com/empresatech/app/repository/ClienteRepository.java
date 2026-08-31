package com.empresatech.app.repository;

import com.empresatech.app.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findByEliminadoFalse();

    Optional<Cliente> findByDni(String dni);
}
