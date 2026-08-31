package com.empresatech.app.controller;

import com.empresatech.app.dto.request.ProductoRequestDTO;
import com.empresatech.app.dto.response.ProductoResponseDTO;
import com.empresatech.app.service.ProductoService;
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
 * Controlador de gestión del catálogo de productos.
 */
@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("productos", productoService.listarProductos());
        return "productos/list";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("producto", new ProductoRequestDTO());
        return "productos/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("producto") ProductoRequestDTO dto,
                          BindingResult result,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        if (result.hasErrors()) {
            model.addAttribute("producto", dto);
            return "productos/form";
        }

        productoService.crearProducto(dto);
        redirectAttributes.addFlashAttribute("successMessage", "Producto guardado correctamente.");
        return "redirect:/productos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable String id, Model model) {
        ProductoResponseDTO producto = productoService.buscarPorId(id);

        ProductoRequestDTO form = ProductoRequestDTO.builder()
                .nombre(producto.getNombre())
                .precioUnitario(producto.getPrecioUnitario())
                .cantidadInicial(producto.getStock() != null ? producto.getStock().getCantidad() : 0)
                .build();

        model.addAttribute("producto", form);
        model.addAttribute("productoId", id);
        return "productos/form";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable String id,
                            @Valid @ModelAttribute("producto") ProductoRequestDTO dto,
                            BindingResult result,
                            RedirectAttributes redirectAttributes,
                            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("productoId", id);
            model.addAttribute("producto", dto);
            return "productos/form";
        }

        productoService.modificarProducto(id, dto);
        redirectAttributes.addFlashAttribute("successMessage", "Producto actualizado correctamente.");
        return "redirect:/productos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id, RedirectAttributes redirectAttributes) {
        productoService.eliminarProducto(id);
        redirectAttributes.addFlashAttribute("successMessage", "Producto eliminado correctamente.");
        return "redirect:/productos";
    }
}
