package com.club.socios.service.impl;

import com.club.socios.domain.Persona;
import com.club.socios.domain.RegistroAcceso;
import com.club.socios.domain.enums.TipoAcceso;
import com.club.socios.dto.RegistroAccesoDTO;
import com.club.socios.exception.RecursoNoEncontradoException;
import com.club.socios.mapper.RegistroAccesoMapper;
import com.club.socios.repository.PersonaRepository;
import com.club.socios.repository.RegistroAccesoRepository;
import com.club.socios.service.RegistroAccesoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * ============================================================================
 * SERVICE: RegistroAcceso (implementación)
 * ============================================================================
 * Implementa la funcionalidad BASE del enunciado: "al ingresar al club el
 * sistema registra el horario de entrada, lo mismo sucede en caso de
 * salida". Funciona tanto para el Socio titular como para cualquier
 * FamiliarSocio, gracias a que ambos son subtipos de la misma superclase
 * Persona (HERENCIA): este servicio programa contra la abstracción
 * {@code PersonaRepository}, sin necesidad de "saber" de qué subtipo se
 * trata.
 * ============================================================================
 */
@Service
@RequiredArgsConstructor
public class RegistroAccesoServiceImpl implements RegistroAccesoService {

    private final RegistroAccesoRepository registroAccesoRepository;
    private final PersonaRepository personaRepository;
    private final RegistroAccesoMapper registroAccesoMapper;

    @Override
    @Transactional
    public RegistroAccesoDTO registrarMovimiento(Long personaId, String puntoAcceso, boolean reconocimientoExitoso) {
        Persona persona = personaRepository.findById(personaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una persona con id " + personaId));

        Optional<RegistroAcceso> ultimo = registroAccesoRepository.findFirstByPersonaIdOrderByFechaHoraDesc(personaId);
        TipoAcceso siguienteTipo = (ultimo.isPresent() && ultimo.get().getTipo() == TipoAcceso.ENTRADA)
                ? TipoAcceso.SALIDA
                : TipoAcceso.ENTRADA;

        RegistroAcceso registro = new RegistroAcceso();
        registro.setPersona(persona);
        registro.setTipo(siguienteTipo);
        registro.setFechaHora(LocalDateTime.now());
        registro.setPuntoAcceso(puntoAcceso);
        registro.setReconocimientoExitoso(reconocimientoExitoso);

        return registroAccesoMapper.toDTO(registroAccesoRepository.save(registro));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RegistroAccesoDTO> historialDePersona(Long personaId) {
        return registroAccesoRepository.findByPersonaIdOrderByFechaHoraDesc(personaId).stream()
                .map(registroAccesoMapper::toDTO)
                .toList();
    }
}
