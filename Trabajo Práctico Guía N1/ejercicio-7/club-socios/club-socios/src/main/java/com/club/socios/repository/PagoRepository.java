package com.club.socios.repository;

import com.club.socios.domain.Pago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PagoRepository extends JpaRepository<Pago, Long> {
    List<Pago> findByCuotaIdOrderByFechaPagoDesc(Long cuotaId);
}
