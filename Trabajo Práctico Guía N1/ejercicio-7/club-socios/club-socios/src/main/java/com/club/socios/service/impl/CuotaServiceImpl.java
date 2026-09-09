package com.club.socios.service.impl;

import com.club.socios.domain.Cuota;
import com.club.socios.domain.GrupoFamiliar;
import com.club.socios.domain.enums.EstadoCuota;
import com.club.socios.dto.CuotaDTO;
import com.club.socios.exception.ReglaDeNegocioException;
import com.club.socios.exception.RecursoNoEncontradoException;
import com.club.socios.mapper.CuotaMapper;
import com.club.socios.repository.CuotaRepository;
import com.club.socios.repository.GrupoFamiliarRepository;
import com.club.socios.service.CuotaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * ============================================================================
 * SERVICE: Cuota (implementación)
 * ============================================================================
 * Implementa la nueva funcionalidad pedida: "registrar el pago de la cuota
 * del club para cada familia". La cuota se genera UNA VEZ por grupo
 * familiar y por período (restricción de unicidad a nivel de tabla, ver
 * {@code Cuota}), y su estado se recalcula automáticamente en base a la
 * suma de los pagos parciales cargados (ver {@link #actualizarEstado}).
 * ============================================================================
 */
@Service
@RequiredArgsConstructor
public class CuotaServiceImpl implements CuotaService {

    private final CuotaRepository cuotaRepository;
    private final GrupoFamiliarRepository grupoFamiliarRepository;
    private final CuotaMapper cuotaMapper;

    @Override
    @Transactional
    public CuotaDTO generar(CuotaDTO dto) {
        GrupoFamiliar grupo = grupoFamiliarRepository.findById(dto.getGrupoFamiliarId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Grupo familiar no encontrado: " + dto.getGrupoFamiliarId()));

        cuotaRepository.findByGrupoFamiliarIdAndPeriodo(grupo.getId(), dto.getPeriodo())
                .ifPresent(c -> {
                    throw new ReglaDeNegocioException(
                            "Ya existe una cuota del período " + dto.getPeriodo() + " para este grupo familiar");
                });

        Cuota cuota = new Cuota();
        cuota.setPeriodo(dto.getPeriodo());
        cuota.setMontoTotal(dto.getMontoTotal());
        cuota.setEstado(EstadoCuota.PENDIENTE);
        cuota.setFechaVencimiento(dto.getFechaVencimiento() != null
                ? dto.getFechaVencimiento()
                : LocalDate.now().plusDays(10));

        // Composición: se agrega la cuota a la colección del grupo familiar
        grupo.agregarCuota(cuota);

        return cuotaMapper.toDTO(cuotaRepository.save(cuota));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CuotaDTO> listarPorGrupo(Long grupoFamiliarId) {
        return cuotaRepository.findByGrupoFamiliarIdOrderByPeriodoDesc(grupoFamiliarId).stream()
                .map(cuotaMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CuotaDTO obtenerPorId(Long id) {
        return cuotaMapper.toDTO(obtenerEntidadPorId(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Cuota obtenerEntidadPorId(Long id) {
        return cuotaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una cuota con id " + id));
    }

    @Override
    @Transactional
    public void actualizarEstado(Cuota cuota) {
        BigDecimal pagado = cuota.getMontoPagado();

        if (pagado.compareTo(BigDecimal.ZERO) == 0) {
            cuota.setEstado(cuota.getFechaVencimiento() != null && cuota.getFechaVencimiento().isBefore(LocalDate.now())
                    ? EstadoCuota.VENCIDA
                    : EstadoCuota.PENDIENTE);
        } else if (pagado.compareTo(cuota.getMontoTotal()) >= 0) {
            cuota.setEstado(EstadoCuota.PAGADA);
        } else {
            cuota.setEstado(EstadoCuota.PARCIAL);
        }
        cuotaRepository.save(cuota);
    }
}
