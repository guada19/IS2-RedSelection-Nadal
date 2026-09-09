package com.club.socios.repository;

import com.club.socios.domain.Cuota;
import com.club.socios.domain.enums.EstadoCuota;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CuotaRepository extends JpaRepository<Cuota, Long> {

    List<Cuota> findByGrupoFamiliarIdOrderByPeriodoDesc(Long grupoFamiliarId);

    Optional<Cuota> findByGrupoFamiliarIdAndPeriodo(Long grupoFamiliarId, String periodo);

    List<Cuota> findByEstado(EstadoCuota estado);
}
