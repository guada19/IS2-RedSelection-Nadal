package com.club.socios.repository;

import com.club.socios.domain.Socio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SocioRepository extends JpaRepository<Socio, Long> {

    Optional<Socio> findByDni(String dni);

    Optional<Socio> findByNumeroSocio(String numeroSocio);

    List<Socio> findByActivoTrue();

    @Query("""
           select s from Socio s
           where lower(s.nombre) like lower(concat('%', :texto, '%'))
              or lower(s.apellido) like lower(concat('%', :texto, '%'))
              or s.dni like concat('%', :texto, '%')
           """)
    List<Socio> buscarPorTexto(String texto);
}
