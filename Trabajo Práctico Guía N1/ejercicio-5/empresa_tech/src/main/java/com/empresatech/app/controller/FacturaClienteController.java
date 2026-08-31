package com.empresatech.app.controller;

import com.empresatech.app.dto.request.DetalleRequestDTO;
import com.empresatech.app.dto.request.FacturaClienteRequestDTO;
import com.empresatech.app.dto.response.ClienteResponseDTO;
import com.empresatech.app.dto.response.FacturaClienteResponseDTO;
import com.empresatech.app.dto.response.ProductoResponseDTO;
import com.empresatech.app.service.ClienteService;
import com.empresatech.app.service.FacturaClienteService;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para ventas y facturación a clientes.
 */
@Controller
@RequestMapping("/ventas")
public class FacturaClienteController {

    private final FacturaClienteService facturaClienteService;
    private final ClienteService clienteService;
    private final ProductoService productoService;

    public FacturaClienteController(FacturaClienteService facturaClienteService,
                                   ClienteService clienteService,
                                   ProductoService productoService) {
        this.facturaClienteService = facturaClienteService;
        this.clienteService = clienteService;
        this.productoService = productoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("ventas", facturaClienteService.listarFacturasCliente());
        return "facturas-cliente/list";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        List<ClienteResponseDTO> clientes = clienteService.listarTodos();
        List<ProductoResponseDTO> productos = productoService.listarProductos();

        FacturaClienteRequestDTO factura = new FacturaClienteRequestDTO();
        factura.setDetalles(new ArrayList<>());
        factura.getDetalles().add(new DetalleRequestDTO());

        model.addAttribute("factura", factura);
        model.addAttribute("clientes", clientes);
        model.addAttribute("productos", productos);
        return "facturas-cliente/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("factura") FacturaClienteRequestDTO dto,
                          BindingResult result,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        if (result.hasErrors() || dto.getDetalles() == null || dto.getDetalles().isEmpty()) {
            model.addAttribute("clientes", clienteService.listarTodos());
            model.addAttribute("productos", productoService.listarProductos());
            model.addAttribute("factura", dto);
            return "facturas-cliente/form";
        }

        FacturaClienteResponseDTO facturaGuardada = facturaClienteService.registrarFacturaCliente(dto);
        redirectAttributes.addFlashAttribute("successMessage", "Venta registrada correctamente.");
        return "redirect:/ventas/detalle/" + facturaGuardada.getId();
    }

    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable String id, Model model) {
        model.addAttribute("factura", facturaClienteService.buscarPorId(id));
        return "facturas-cliente/detalle";
    }
}

//http://localhost:8080/compras
//http://localhost/phpmyadmin
