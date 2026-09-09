package com.club.socios.service;

import com.club.socios.domain.RegistroAcceso;
import com.club.socios.domain.Socio;
import com.club.socios.domain.enums.TipoAcceso;
import com.club.socios.mapper.RegistroAccesoMapper;
import com.club.socios.repository.PersonaRepository;
import com.club.socios.repository.RegistroAccesoRepository;
import com.club.socios.service.impl.RegistroAccesoServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * ============================================================================
 * PRUEBAS UNITARIAS - RegistroAccesoService
 * ============================================================================
 * Cubre la regla funcional central del enunciado base: el sistema debe
 * alternar automáticamente ENTRADA/SALIDA según el último movimiento
 * registrado por esa persona (sea Socio o FamiliarSocio, gracias a la
 * herencia común de Persona).
 * ============================================================================
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("RegistroAccesoService - pruebas unitarias")
class RegistroAccesoServiceTest {

    @Mock
    private RegistroAccesoRepository registroAccesoRepository;
    @Mock
    private PersonaRepository personaRepository;
    @Mock
    private RegistroAccesoMapper registroAccesoMapper;

    @InjectMocks
    private RegistroAccesoServiceImpl registroAccesoService;

    @Test
    @DisplayName("Si la persona no tiene movimientos previos, el primer registro debe ser ENTRADA")
    void registrarMovimiento_sinHistorial_generaEntrada() {
        Socio socio = new Socio();
        socio.setId(1L);
        when(personaRepository.findById(1L)).thenReturn(Optional.of(socio));
        when(registroAccesoRepository.findFirstByPersonaIdOrderByFechaHoraDesc(1L)).thenReturn(Optional.empty());
        when(registroAccesoRepository.save(any(RegistroAcceso.class))).thenAnswer(inv -> inv.getArgument(0));

        registroAccesoService.registrarMovimiento(1L, "Molinete Principal", true);

        ArgumentCaptor<RegistroAcceso> captor = ArgumentCaptor.forClass(RegistroAcceso.class);
        org.mockito.Mockito.verify(registroAccesoRepository).save(captor.capture());
        assertThat(captor.getValue().getTipo()).isEqualTo(TipoAcceso.ENTRADA);
    }

    @Test
    @DisplayName("Si el último movimiento fue ENTRADA, el siguiente debe ser SALIDA")
    void registrarMovimiento_ultimaEntrada_generaSalida() {
        Socio socio = new Socio();
        socio.setId(1L);
        RegistroAcceso ultimaEntrada = new RegistroAcceso();
        ultimaEntrada.setTipo(TipoAcceso.ENTRADA);

        when(personaRepository.findById(1L)).thenReturn(Optional.of(socio));
        when(registroAccesoRepository.findFirstByPersonaIdOrderByFechaHoraDesc(1L))
                .thenReturn(Optional.of(ultimaEntrada));
        when(registroAccesoRepository.save(any(RegistroAcceso.class))).thenAnswer(inv -> inv.getArgument(0));

        registroAccesoService.registrarMovimiento(1L, "Molinete Principal", true);

        ArgumentCaptor<RegistroAcceso> captor = ArgumentCaptor.forClass(RegistroAcceso.class);
        org.mockito.Mockito.verify(registroAccesoRepository).save(captor.capture());
        assertThat(captor.getValue().getTipo()).isEqualTo(TipoAcceso.SALIDA);
    }
}
