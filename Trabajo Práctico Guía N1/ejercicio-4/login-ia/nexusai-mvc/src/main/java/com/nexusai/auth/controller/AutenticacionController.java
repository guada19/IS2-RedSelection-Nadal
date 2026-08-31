package com.nexusai.auth.controller;

import com.nexusai.auth.dto.LoginDTO;
import com.nexusai.auth.dto.RegistroUsuarioDTO;
import com.nexusai.auth.exception.CredencialesInvalidasException;
import com.nexusai.auth.exception.DatoYaRegistradoException;
import com.nexusai.auth.exception.UsuarioBloqueadoException;
import com.nexusai.auth.exception.UsuarioNoRegistradoException;
import com.nexusai.auth.model.Usuario;
import com.nexusai.auth.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * ============================================================================
 * CONTROLADOR — AutenticacionController (la "C" de MVC)
 * ============================================================================
 * Es el "Front/Application Controller" de este módulo: recibe las peticiones
 * HTTP que llegan al DispatcherServlet de Spring, las delega en la capa de
 * SERVICIO (nunca contiene lógica de negocio propia) y decide qué plantilla
 * Thymeleaf (VISTA) renderizar, o hacia dónde redirigir.
 *
 * ANOTACIONES DE SPRING MVC:
 *
 *  @Controller           A diferencia de @RestController, este estereotipo
 *                        indica que los métodos devuelven NOMBRES LÓGICOS DE
 *                        VISTA (String), que el ViewResolver de Thymeleaf
 *                        traduce a un archivo HTML dentro de
 *                        src/main/resources/templates/ (según
 *                        spring.thymeleaf.prefix/suffix).
 *
 *  @GetMapping / @PostMapping
 *                        Asocian una URL + verbo HTTP a un método Java.
 *                        Se usa GET para mostrar formularios (idempotente,
 *                        sin efectos secundarios) y POST para procesarlos
 *                        (con efectos secundarios: alta en BD, login, etc.)
 *                        siguiendo el patrón Post/Redirect/Get.
 *
 *  @Valid + BindingResult
 *                        @Valid dispara las anotaciones de Bean Validation
 *                        declaradas en el DTO. BindingResult (debe declararse
 *                        INMEDIATAMENTE después del objeto validado) captura
 *                        los errores en vez de lanzar una excepción, para que
 *                        el controller pueda decidir volver a mostrar el
 *                        formulario con los mensajes de error.
 *
 *  Model                 Es el "puente" de datos entre el Controller y la
 *                        VISTA: todo atributo agregado con model.addAttribute
 *                        queda disponible en la plantilla Thymeleaf mediante
 *                        la sintaxis ${...}.
 *
 *  RedirectAttributes    Permite adjuntar mensajes "flash" que sobreviven a
 *                        una redirección HTTP (patrón Post/Redirect/Get),
 *                        evitando el reenvío del formulario al refrescar.
 *
 *  HttpSession           Se usa para mantener el estado de "usuario logueado"
 *                        entre peticiones (el protocolo HTTP es sin estado).
 * ============================================================================
 */
@Controller
public class AutenticacionController {

    /** Clave con la que se guarda el usuario autenticado dentro de la sesión HTTP. */
    private static final String SESSION_USUARIO = "usuarioLogueado";

    private final UsuarioService usuarioService;

    public AutenticacionController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // ==========================================================================
    // RAÍZ DEL SITIO
    // ==========================================================================

    /** La página de inicio redirige directamente al formulario de login. */
    @GetMapping("/")
    public String raiz() {
        return "redirect:/login";
    }

    // ==========================================================================
    // LOGIN
    // ==========================================================================

    /**
     * GET /login — Muestra el formulario de inicio de sesión.
     * Si ya existe una sesión activa, se redirige directamente a /home.
     */
    @GetMapping("/login")
    public String mostrarLogin(HttpSession session, Model model) {
        if (session.getAttribute(SESSION_USUARIO) != null) {
            return "redirect:/home";
        }
        // Se agrega un LoginDTO vacío para poder usar th:object en la vista.
        if (!model.containsAttribute("loginForm")) {
            model.addAttribute("loginForm", new LoginDTO());
        }
        return "login"; // -> templates/login.html
    }

    /**
     * POST /login — Procesa las credenciales enviadas por el formulario.
     * Aplica el patrón Post/Redirect/Get: sea cual sea el resultado, se
     * responde siempre con un "redirect:" para evitar el reenvío del POST
     * si el usuario refresca la página (F5).
     */
    @PostMapping("/login")
    public String procesarLogin(@Valid @ModelAttribute("loginForm") LoginDTO loginForm,
                                 BindingResult bindingResult,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {

        // Validaciones básicas de formato (campos vacíos, etc.)
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Complete correo y clave.");
            return "redirect:/login";
        }

        try {
            // Toda la lógica de negocio (existencia, bloqueo, intentos) vive
            // en la capa Service; el Controller sólo reacciona al resultado.
            Usuario usuario = usuarioService.login(loginForm.getCorreoPersonal(), loginForm.getClave());

            // Login correcto: se guarda el usuario en la sesión HTTP para que
            // las siguientes peticiones (ej. /home) sepan que está autenticado.
            session.setAttribute(SESSION_USUARIO, usuario);
            return "redirect:/home";

        } catch (UsuarioNoRegistradoException ex) {
            // Regla del enunciado: si no está registrado, se lo invita a
            // registrarse, precargando el correo que ya había tipeado.
            // Se arma un RegistroUsuarioDTO parcial y se envía como atributo
            // "flash" con el mismo nombre ("registroForm") que espera la
            // vista de registro, de forma que quede precargado en el <input>
            // de correo sin necesidad de lógica extra en el template.
            RegistroUsuarioDTO precarga = new RegistroUsuarioDTO();
            precarga.setCorreoPersonal(loginForm.getCorreoPersonal());
            redirectAttributes.addFlashAttribute("registroForm", precarga);
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/registro";

        } catch (UsuarioBloqueadoException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/login";

        } catch (CredencialesInvalidasException ex) {
            redirectAttributes.addFlashAttribute("error",
                    ex.getMessage() + " Le queda(n) " + ex.getIntentosRestantes() + " intento(s).");
            return "redirect:/login";
        }
    }

    // ==========================================================================
    // REGISTRO
    // ==========================================================================

    /** GET /registro — Muestra el formulario de alta de usuario. */
    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        if (!model.containsAttribute("registroForm")) {
            model.addAttribute("registroForm", new RegistroUsuarioDTO());
        }
        return "registro"; // -> templates/registro.html
    }

    /**
     * POST /registro — Procesa el alta de un nuevo usuario.
     * Si hay errores de validación (@Valid) o de negocio (correo/documento
     * duplicado, claves que no coinciden), se vuelve a mostrar el mismo
     * formulario conservando lo tipeado y mostrando los mensajes de error.
     */
    @PostMapping("/registro")
    public String procesarRegistro(@Valid @ModelAttribute("registroForm") RegistroUsuarioDTO registroForm,
                                    BindingResult bindingResult,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            // Se vuelve a renderizar la MISMA vista (no un redirect) para
            // conservar automáticamente, vía BindingResult, los errores de
            // campo que th:errors mostrará junto a cada input.
            return "registro";
        }

        if (!registroForm.lasClavesCoinciden()) {
            model.addAttribute("error", "Las claves ingresadas no coinciden.");
            return "registro";
        }

        try {
            usuarioService.registrar(registroForm);
            redirectAttributes.addFlashAttribute("mensaje",
                    "¡Registro exitoso! Ya puede iniciar sesión con su correo y clave.");
            return "redirect:/login";

        } catch (DatoYaRegistradoException ex) {
            model.addAttribute("error", ex.getMessage());
            return "registro";
        }
    }

    // ==========================================================================
    // HOME (área privada, requiere sesión iniciada)
    // ==========================================================================

    /**
     * GET /home — Página de bienvenida tras un login exitoso.
     * Se controla manualmente la presencia de sesión (en lugar de usar
     * Spring Security) para mantener el ejercicio enfocado en los conceptos
     * de MVC + ORM pedidos por la consigna.
     */
    @GetMapping("/home")
    public String mostrarHome(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute(SESSION_USUARIO);
        if (usuario == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuario", usuario);
        return "home"; // -> templates/home.html
    }

    /** GET /logout — Cierra la sesión del usuario y vuelve al login. */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
