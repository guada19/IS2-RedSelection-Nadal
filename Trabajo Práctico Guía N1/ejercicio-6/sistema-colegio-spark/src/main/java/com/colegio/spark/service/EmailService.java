package com.colegio.spark.service;

import com.colegio.spark.model.Docente;

/**
 * Contrato de la capa Service para el envio de correos electronicos.
 * Se programa contra la interfaz (no contra EmailServiceImpl) para respetar
 * el principio de inversion de dependencias: quien la use (DocenteServiceImpl)
 * no conoce los detalles de JavaMailSender ni de la plantilla del correo.
 */
public interface EmailService {

    /**
     * Envia el correo de bienvenida al correo personal de un docente recien
     * registrado en el sistema.
     */
    void enviarCorreoBienvenida(Docente docente);

}
