package com.club.socios.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * ============================================================================
 * PRUEBA DE INTEGRACIÓN (Spring Boot Test + MockMvc)
 * ============================================================================
 * A diferencia de las pruebas unitarias (que aíslan una clase con mocks),
 * esta prueba levanta el contexto COMPLETO de Spring (controladores,
 * servicios, repositorios, seguridad) contra la base H2 en memoria del
 * perfil "test", validando que todas las capas realmente encajan:
 * MVC + Security + ORM funcionando juntos de punta a punta.
 * ============================================================================
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SocioControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Sin autenticar, el acceso a /socios debe redirigir a /login")
    void accesoSinAutenticar_redirigeALogin() throws Exception {
        mockMvc.perform(get("/socios"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void listadoDeSocios_conUsuarioAutenticado_devuelve200() throws Exception {
        mockMvc.perform(get("/socios"))
                .andExpect(status().isOk())
                .andExpect(view().name("socios/lista"));
    }

    @Test
    @WithMockUser(username = "recepcion", authorities = "ROLE_RECEPCIONISTA")
    void altaDeSocio_conRolRecepcionista_esRechazada() throws Exception {
        // Regla de negocio de seguridad: sólo ROLE_ADMIN puede dar de alta socios
        mockMvc.perform(get("/socios/nuevo"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void altaDeSocio_conDatosValidos_creaYRedirige() throws Exception {
        mockMvc.perform(post("/socios")
                        .with(csrf())
                        .param("nombre", "Lucía")
                        .param("apellido", "Pérez")
                        .param("dni", "40555666")
                        .param("categoria", "Individual"))
                .andExpect(status().is3xxRedirection());
    }
}
