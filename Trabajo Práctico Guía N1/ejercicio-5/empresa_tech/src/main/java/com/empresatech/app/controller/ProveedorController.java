package com.empresatech.app.controller;

import com.empresatech.app.dto.request.ProveedorRequestDTO;
import com.empresatech.app.dto.response.ProveedorResponseDTO;
import com.empresatech.app.service.ProveedorService;
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
 * Controlador para la gestión de proveedores.
 */
@Controller
@RequestMapping("/proveedores")
public class ProveedorController {

    private final ProveedorService proveedorService;

    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("proveedores", proveedorService.listarTodos());
        return "proveedores/list";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("proveedor", new ProveedorRequestDTO());
        return "proveedores/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("proveedor") ProveedorRequestDTO dto,
                          BindingResult result,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        if (result.hasErrors()) {
            model.addAttribute("proveedor", dto);
            return "proveedores/form";
        }

        proveedorService.crearProveedor(dto);
        redirectAttributes.addFlashAttribute("successMessage", "Proveedor registrado correctamente.");
        return "redirect:/proveedores";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable String id, Model model) {
        ProveedorResponseDTO proveedor = proveedorService.buscarPorId(id);
        ProveedorRequestDTO form = ProveedorRequestDTO.builder()
                .cuit(proveedor.getCuit())
                .razonSocial(proveedor.getRazonSocial())
                .telefono(proveedor.getTelefono())
                .build();

        model.addAttribute("proveedor", form);
        model.addAttribute("proveedorId", id);
        return "proveedores/form";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable String id,
                            @Valid @ModelAttribute("proveedor") ProveedorRequestDTO dto,
                            BindingResult result,
                            RedirectAttributes redirectAttributes,
                            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("proveedorId", id);
            model.addAttribute("proveedor", dto);
            return "proveedores/form";
        }

        proveedorService.modificarProveedor(id, dto);
        redirectAttributes.addFlashAttribute("successMessage", "Proveedor actualizado correctamente.");
        return "redirect:/proveedores";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id, RedirectAttributes redirectAttributes) {
        proveedorService.eliminarProveedor(id);
        redirectAttributes.addFlashAttribute("successMessage", "Proveedor dado de baja correctamente.");
        return "redirect:/proveedores";
    }
}
