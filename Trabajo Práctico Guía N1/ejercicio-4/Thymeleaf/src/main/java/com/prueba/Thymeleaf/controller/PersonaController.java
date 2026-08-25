package com.prueba.Thymeleaf.controller;

import com.prueba.Thymeleaf.model.Persona;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PersonaController {

    @GetMapping("/formulario")
    public String mostrarFormulario(Model model) {
        model.addAttribute("persona", new Persona());
        return "formulario"; //nombre de la vista
    }

    @PostMapping("/procesar")
    public String procesarFormulario(
            /*@RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam int edad,*/
            Persona persona, //recibo un objeto en vez de los parametros por separado
            Model model) {
        model.addAttribute(persona);


        if (persona.getEdad() >= 18) {
            model.addAttribute("tipoEdad", "Mayor de edad");
        } else {
            model.addAttribute("tipoEdad", "Menor de edad");
        }

        return "resultado";
    }

}