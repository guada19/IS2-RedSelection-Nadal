package com.colegio.spark.controller;

import com.colegio.spark.dto.request.DocenteRegistroDTO;
import com.colegio.spark.exception.EmailYaRegistradoException;
import com.colegio.spark.service.DocenteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * ============================================================================
 *  AuthController                (capa Controller - MVC)
 * ============================================================================
 * Controlador publico (sin autenticacion previa) que resuelve:
 *   - GET  /login    -> muestra la pantalla de inicio de sesion (Thymeleaf).
 *                        El procesamiento del POST del login NO pasa por este
 *                        metodo: lo intercepta directamente el filtro de
 *                        Spring Security configurado en SecurityConfig
 *                        (formLogin().loginProcessingUrl("/login")).
 *   - GET  /registro  -> muestra el formulario de auto-registro de un docente.
 *   - POST /registro  -> procesa el alta del docente.
 *
 * PATRON MVC EN ESTE CONTROLLER:
 *   Model  -> DocenteRegistroDTO (los datos que trae/necesita el formulario)
 *   View   -> plantillas Thymeleaf en templates/auth/*.html
 *   Controller -> esta clase: recibe la peticion HTTP, invoca al Service
 *                 (nunca contiene logica de negocio ni accede al Repository
 *                 directamente) y decide que vista renderizar o a que URL
 *                 redirigir.
 *
 * @Controller (no @RestController): los metodos devuelven el NOMBRE LOGICO
 *   de una vista Thymeleaf (String), no datos serializados (JSON), que es la
 *   esencia de una aplicacion MVC clasica con vistas server-side.
 * ============================================================================
 */
@Controller
@RequiredArgsConstructor
public class AuthController {

    private final DocenteService docenteService;

    @GetMapping("/login")
    public String mostrarLogin() {
        return "auth/login";
    }

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("docenteRegistroDTO", new DocenteRegistroDTO());
        return "auth/registro";
    }

    @PostMapping("/registro")
    public String registrar(@Valid @ModelAttribute("docenteRegistroDTO") DocenteRegistroDTO dto,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        // Si Bean Validation (las anotaciones @NotBlank, @Email, etc. del DTO)
        // encontro errores, se vuelve a mostrar el mismo formulario con los
        // mensajes correspondientes, sin llegar a tocar la capa Service.
        if (bindingResult.hasErrors()) {
            return "auth/registro";
        }

        if (!dto.getPassword().equals(dto.getConfirmarPassword())) {
            model.addAttribute("errorConfirmacion", "Las contraseñas ingresadas no coinciden");
            return "auth/registro";
        }

        try {
            docenteService.registrarDocente(dto);
        } catch (EmailYaRegistradoException ex) {
            model.addAttribute("errorEmail", ex.getMessage());
            return "auth/registro";
        }

        redirectAttributes.addFlashAttribute("mensajeExito",
                "Registro exitoso. Te enviamos un correo de bienvenida. Ya podes iniciar sesion.");
        return "redirect:/login";
    }

}
