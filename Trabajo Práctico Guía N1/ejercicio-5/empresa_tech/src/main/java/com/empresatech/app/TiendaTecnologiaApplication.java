package com.empresatech.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación.
 *
 * @SpringBootApplication es una anotación "compuesta" que combina:
 *   - @Configuration: marca la clase como fuente de definiciones de beans.
 *   - @EnableAutoConfiguration: le dice a Spring Boot que configure
 *     automáticamente el contexto según las dependencias presentes en el
 *     classpath (por ejemplo, configura Hibernate/JPA porque detecta
 *     spring-boot-starter-data-jpa).
 *   - @ComponentScan: escanea el paquete actual y sus subpaquetes en busca
 *     de componentes de Spring (@Component, @Service, @Repository,
 *     @Controller, etc.) para registrarlos en el contexto de la aplicación.
 */
@SpringBootApplication
public class TiendaTecnologiaApplication {

    /**
     * Punto de entrada de la aplicación. SpringApplication.run() arranca
     * el contenedor de Spring, el servidor Tomcat embebido y realiza toda
     * la configuración automática (incluida la conexión a MySQL definida
     * en application.properties).
     */
    public static void main(String[] args) {
        SpringApplication.run(TiendaTecnologiaApplication.class, args);
    }
}

// sudo /opt/lampp/manager-linux-x64.run
// http://localhost/phpmyadmin
// http://localhost:8080/login

