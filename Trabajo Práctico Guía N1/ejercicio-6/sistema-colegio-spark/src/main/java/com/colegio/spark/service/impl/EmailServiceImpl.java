package com.colegio.spark.service.impl;

import com.colegio.spark.model.Docente;
import com.colegio.spark.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * ============================================================================
 *  EmailServiceImpl              (capa Service - notificaciones)
 * ============================================================================
 * Implementacion concreta de EmailService, basada en JavaMailSender (provisto
 * automaticamente por Spring Boot gracias a la dependencia
 * spring-boot-starter-mail y a la configuracion spring.mail.* de
 * application.properties).
 *
 *  @Async
 *      El envio de un correo es una operacion de red que puede demorar o
 *      fallar (timeout del servidor SMTP, etc.). Se marca como @Async para
 *      que se ejecute en un hilo aparte y NO bloquee ni haga fallar el flujo
 *      principal de registro del docente (el docente no deberia tener que
 *      esperar a que el mail salga para ver la confirmacion de su alta). Para
 *      que @Async tenga efecto, se habilita @EnableAsync en la configuracion
 *      (ver config/AsyncConfig.java).
 *
 *  app.mail.fallar-silenciosamente
 *      Propiedad de application.properties: si el envio de correo falla (por
 *      ejemplo, credenciales SMTP no configuradas en el ambiente de
 *      desarrollo/TP), el error se registra en el log pero NO se relanza,
 *      para no impedir que el registro del docente se complete igualmente.
 * ============================================================================
 */
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;

    @Value("${app.mail.remitente}")
    private String remitente;

    @Value("${app.mail.fallar-silenciosamente:true}")
    private boolean fallarSilenciosamente;

    @Override
    @Async
    public void enviarCorreoBienvenida(Docente docente) {
        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setFrom(remitente);
            mensaje.setTo(docente.getEmail());
            mensaje.setSubject("Bienvenido/a al Sistema de Gestion Escolar - Colegio Spark");
            mensaje.setText(construirCuerpoDelMensaje(docente));
            mailSender.send(mensaje);
            log.info("Correo de bienvenida enviado a {}", docente.getEmail());
        } catch (Exception ex) {
            log.error("No se pudo enviar el correo de bienvenida a {}: {}",
                    docente.getEmail(), ex.getMessage());
            if (!fallarSilenciosamente) {
                throw ex;
            }
        }
    }

    private String construirCuerpoDelMensaje(Docente docente) {
        return """
                Hola %s %s,

                Tu cuenta como docente en el Sistema de Gestion Escolar del Colegio Spark
                fue creada exitosamente.

                Tu usuario de acceso es tu correo personal: %s

                Ya podes iniciar sesion y, si lo deseas, cambiar tu contraseña desde la
                seccion "Mi perfil" una vez que ingreses al sistema.

                Saludos,
                Colegio Spark
                """.formatted(docente.getNombre(), docente.getApellido(), docente.getEmail());
    }

}
