package com.colegio.spark.service;

import com.colegio.spark.dto.request.CambioPasswordDTO;
import com.colegio.spark.dto.request.DocenteEdicionDTO;
import com.colegio.spark.dto.request.DocenteRegistroDTO;
import com.colegio.spark.dto.response.DocenteResponseDTO;

import java.util.List;

/**
 * Contrato de la capa Service para las operaciones de negocio relacionadas
 * con Docente: auto-registro (con envio de correo de bienvenida), consulta,
 * edicion de datos personales, cambio de contraseña y activacion/baja logica.
 */
public interface DocenteService {

    /** Registra un nuevo docente (rol DOCENTE, activo=true) y envia el correo de bienvenida. */
    DocenteResponseDTO registrarDocente(DocenteRegistroDTO dto);

    List<DocenteResponseDTO> listarTodos();

    DocenteResponseDTO buscarPorId(Long id);

    DocenteResponseDTO buscarPorEmail(String email);

    DocenteResponseDTO actualizarDatosPersonales(Long id, DocenteEdicionDTO dto);

    /** Cambia la contraseña de un docente, verificando primero la contraseña actual. */
    void cambiarPassword(String email, CambioPasswordDTO dto);

    void cambiarEstadoActivo(Long id, boolean activo);

}
