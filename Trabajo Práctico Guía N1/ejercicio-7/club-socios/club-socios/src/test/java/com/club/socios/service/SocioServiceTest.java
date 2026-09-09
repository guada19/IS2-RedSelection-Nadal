package com.club.socios.service;

import com.club.socios.domain.Socio;
import com.club.socios.dto.SocioDTO;
import com.club.socios.exception.ReglaDeNegocioException;
import com.club.socios.exception.RecursoNoEncontradoException;
import com.club.socios.mapper.SocioMapper;
import com.club.socios.repository.GrupoFamiliarRepository;
import com.club.socios.repository.SocioRepository;
import com.club.socios.service.impl.SocioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * ============================================================================
 * PRUEBAS UNITARIAS - SocioService
 * ============================================================================
 * Se aíslan las dependencias (Repository, Mapper, ImagenRostroService) con
 * Mockito para probar SOLO la lógica de negocio del Service, sin levantar
 * el contexto de Spring ni tocar una base de datos real (test rápido y
 * determinístico, típico de la pirámide de testing: muchas unitarias,
 * pocas de integración).
 * ============================================================================
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SocioService - pruebas unitarias")
class SocioServiceTest {

    @Mock
    private SocioRepository socioRepository;
    @Mock
    private GrupoFamiliarRepository grupoFamiliarRepository;
    @Mock
    private SocioMapper socioMapper;
    @Mock
    private ImagenRostroService imagenRostroService;

    @InjectMocks
    private SocioServiceImpl socioService;

    private SocioDTO dtoValido;

    @BeforeEach
    void setUp() {
        dtoValido = SocioDTO.builder()
                .nombre("Ana")
                .apellido("Gómez")
                .dni("30123456")
                .fechaNacimiento(LocalDate.of(1990, 1, 1))
                .fechaAlta(LocalDate.now())
                .categoria("Familiar")
                .build();
    }

    @Test
    @DisplayName("crear() debe rechazar un DNI ya registrado (regla de negocio)")
    void crear_conDniDuplicado_lanzaExcepcion() {
        when(socioRepository.findByDni("30123456")).thenReturn(Optional.of(new Socio()));

        assertThatThrownBy(() -> socioService.crear(dtoValido, null))
                .isInstanceOf(ReglaDeNegocioException.class)
                .hasMessageContaining("30123456");

        verify(socioRepository, never()).save(any());
    }

    @Test
    @DisplayName("crear() debe persistir el Socio y crear automáticamente su GrupoFamiliar (composición)")
    void crear_conDatosValidos_creaSocioYGrupoFamiliar() {
        when(socioRepository.findByDni("30123456")).thenReturn(Optional.empty());
        when(socioRepository.save(any(Socio.class))).thenAnswer(inv -> {
            Socio s = inv.getArgument(0);
            s.setId(1L);
            return s;
        });
        when(socioMapper.toDTO(any(Socio.class))).thenReturn(dtoValido);

        SocioDTO resultado = socioService.crear(dtoValido, null);

        assertThat(resultado).isNotNull();

        // Verifica que efectivamente se haya guardado el GrupoFamiliar asociado
        // al socio recién creado (relación de COMPOSICIÓN Socio-GrupoFamiliar).
        ArgumentCaptor<com.club.socios.domain.GrupoFamiliar> captor =
                ArgumentCaptor.forClass(com.club.socios.domain.GrupoFamiliar.class);
        verify(grupoFamiliarRepository).save(captor.capture());
        assertThat(captor.getValue().getSocioTitular().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("obtenerEntidadPorId() debe lanzar RecursoNoEncontradoException si no existe")
    void obtenerEntidadPorId_inexistente_lanzaExcepcion() {
        when(socioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> socioService.obtenerEntidadPorId(99L))
                .isInstanceOf(RecursoNoEncontradoException.class);
    }

    @Test
    @DisplayName("eliminar() debe hacer baja lógica (activo=false), no borrado físico")
    void eliminar_hacerBajaLogica() {
        Socio socio = new Socio();
        socio.setId(5L);
        socio.setActivo(true);
        when(socioRepository.findById(5L)).thenReturn(Optional.of(socio));
        when(socioRepository.save(any(Socio.class))).thenReturn(socio);

        socioService.eliminar(5L);

        assertThat(socio.isActivo()).isFalse();
        verify(socioRepository).save(socio);
        verify(socioRepository, never()).delete(any());
        verify(socioRepository, never()).deleteById(any());
    }
}
