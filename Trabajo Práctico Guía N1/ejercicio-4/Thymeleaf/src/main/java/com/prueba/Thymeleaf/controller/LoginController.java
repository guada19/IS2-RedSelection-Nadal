package com.prueba.Thymeleaf.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {
    //Base de datos lógica
    private Map<String, String> usuarios = new HashMap<>();

    public LoginController() {

        usuarios.put("Luisina", "1234");
        usuarios.put("TodoCode", "java");
        usuarios.put("Ibra", "perro");
        usuarios.put("Colapinto", "f1");
    }
    //login

    @GetMapping("/login")
    public String mostrarLogin(){
        return "login";
    }

    @PostMapping("/login") //enviar desde el login
    //inicio de sesion
    public String procesarLogin(@RequestParam String nombreUsuario,
                                @RequestParam String password,
                                HttpSession session,
                                Model model){

        //validar que el usuario existe
        if (usuarios.containsKey(nombreUsuario)){
            //si existe validar la contraseña
            String passwordRecibida = usuarios.get(nombreUsuario);
            if(passwordRecibida.equals(password)) {
                session.setAttribute("usuarioLogueado", nombreUsuario);
                //mantener la sesión activa redirigiendo con los datos actuales
                return("redirect:/bienvenida");
            }

        }

        model.addAttribute("error", "Usuario o contraseña erronea");
        return "login";
    }

    //pag bienvenida
    @GetMapping("/bienvenida")
    public String mostrarBienvenida (HttpSession session) {
        String usuario = (String) session.getAttribute("usuarioLogueado");
        if (usuario == null) {
            return "redirect:/login";
        }
        return "bienvenida";
    }

    //cerrar sesion
    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

}
