package com.club.socios.repository;

import com.club.socios.domain.GrupoFamiliar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GrupoFamiliarRepository extends JpaRepository<GrupoFamiliar, Long> {
    Optional<GrupoFamiliar> findBySocioTitularId(Long socioId);
}
