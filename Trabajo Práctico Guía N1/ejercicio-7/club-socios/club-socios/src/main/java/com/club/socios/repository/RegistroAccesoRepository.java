package com.club.socios.repository;

import com.club.socios.domain.RegistroAcceso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RegistroAccesoRepository extends JpaRepository<RegistroAcceso, Long> {

    List<RegistroAcceso> findByPersonaIdOrderByFechaHoraDesc(Long personaId);

    /** Trae el último movimiento (entrada o salida) de una persona, para saber qué botón habilitar. */
    Optional<RegistroAcceso> findFirstByPersonaIdOrderByFechaHoraDesc(Long personaId);

    List<RegistroAcceso> findByFechaHoraBetweenOrderByFechaHoraDesc(LocalDateTime desde, LocalDateTime hasta);
}
