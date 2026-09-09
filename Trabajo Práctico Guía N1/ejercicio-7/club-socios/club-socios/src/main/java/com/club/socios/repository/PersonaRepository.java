package com.club.socios.repository;

import com.club.socios.domain.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio ORM (Spring Data JPA) para la superclase Persona. Sirve para
 * operaciones transversales (ej: buscar por DNI sin importar si es Socio o
 * FamiliarSocio) que aprovechan la HERENCIA con estrategia JOINED.
 */
public interface PersonaRepository extends JpaRepository<Persona, Long> {
    Optional<Persona> findByDni(String dni);
}
