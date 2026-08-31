package com.empresatech.app.controller;

import com.empresatech.app.dto.request.ClienteRequestDTO;
import com.empresatech.app.dto.response.ClienteResponseDTO;
import com.empresatech.app.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador para la gestión de clientes.
 */
@Controller
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("clientes", clienteService.listarTodos());
        return "clientes/list";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("cliente", new ClienteRequestDTO());
        return "clientes/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("cliente") ClienteRequestDTO dto,
                          BindingResult result,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        if (result.hasErrors()) {
            model.addAttribute("cliente", dto);
            return "clientes/form";
        }

        clienteService.crearCliente(dto);
        redirectAttributes.addFlashAttribute("successMessage", "Cliente registrado correctamente.");
        return "redirect:/clientes";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable String id, Model model) {
        ClienteResponseDTO cliente = clienteService.buscarPorId(id);
        ClienteRequestDTO form = ClienteRequestDTO.builder()
                .nombre(cliente.getNombre())
                .apellido(cliente.getApellido())
                .dni(cliente.getDni())
                .telefono(cliente.getTelefono())
                .build();

        model.addAttribute("cliente", form);
        model.addAttribute("clienteId", id);
        return "clientes/form";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable String id,
                            @Valid @ModelAttribute("cliente") ClienteRequestDTO dto,
                            BindingResult result,
                            RedirectAttributes redirectAttributes,
                            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("clienteId", id);
            model.addAttribute("cliente", dto);
            return "clientes/form";
        }

        clienteService.modificarCliente(id, dto);
        redirectAttributes.addFlashAttribute("successMessage", "Cliente actualizado correctamente.");
        return "redirect:/clientes";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id, RedirectAttributes redirectAttributes) {
        clienteService.eliminarCliente(id);
        redirectAttributes.addFlashAttribute("successMessage", "Cliente dado de baja correctamente.");
        return "redirect:/clientes";
    }
}
