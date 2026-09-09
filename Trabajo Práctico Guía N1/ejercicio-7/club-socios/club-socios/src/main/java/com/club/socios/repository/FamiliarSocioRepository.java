package com.club.socios.repository;

import com.club.socios.domain.FamiliarSocio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FamiliarSocioRepository extends JpaRepository<FamiliarSocio, Long> {
    List<FamiliarSocio> findByGrupoFamiliarId(Long grupoFamiliarId);

    /**
     * Proyección directa vía JPQL: resuelve el id del socio titular del
     * grupo familiar de un Familiar EN UNA SOLA CONSULTA, sin necesidad de
     * inicializar las asociaciones lazy fuera de una transacción (evita
     * LazyInitializationException al usarse desde el Controller, ya que
     * {@code spring.jpa.open-in-view=false}).
     */
    @Query("select f.grupoFamiliar.socioTitular.id from FamiliarSocio f where f.id = :id")
    Optional<Long> buscarIdSocioTitular(@Param("id") Long id);
}
