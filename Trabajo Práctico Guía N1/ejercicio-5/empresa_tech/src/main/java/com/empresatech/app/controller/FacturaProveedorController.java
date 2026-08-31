package com.empresatech.app.controller;

import com.empresatech.app.dto.request.DetalleRequestDTO;
import com.empresatech.app.dto.request.FacturaProveedorRequestDTO;
import com.empresatech.app.dto.response.ProductoResponseDTO;
import com.empresatech.app.dto.response.ProveedorResponseDTO;
import com.empresatech.app.service.FacturaProveedorService;
import com.empresatech.app.service.ProductoService;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para compras y facturación a proveedores.
 */
@Controller
@RequestMapping("/compras")
public class FacturaProveedorController {

    private final FacturaProveedorService facturaProveedorService;
    private final ProveedorService proveedorService;
    private final ProductoService productoService;

    public FacturaProveedorController(FacturaProveedorService facturaProveedorService,
                                     ProveedorService proveedorService,
                                     ProductoService productoService) {
        this.facturaProveedorService = facturaProveedorService;
        this.proveedorService = proveedorService;
        this.productoService = productoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("compras", facturaProveedorService.listarFacturasProveedor());
        return "facturas-proveedor/list";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        List<ProveedorResponseDTO> proveedores = proveedorService.listarTodos();
        List<ProductoResponseDTO> productos = productoService.listarProductos();

        FacturaProveedorRequestDTO factura = new FacturaProveedorRequestDTO();
        factura.setDetalles(new ArrayList<>());
        factura.getDetalles().add(new DetalleRequestDTO());

        model.addAttribute("factura", factura);
        model.addAttribute("proveedores", proveedores);
        model.addAttribute("productos", productos);
        return "facturas-proveedor/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("factura") FacturaProveedorRequestDTO dto,
                          BindingResult result,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        if (result.hasErrors() || dto.getDetalles() == null || dto.getDetalles().isEmpty()) {
            model.addAttribute("proveedores", proveedorService.listarTodos());
            model.addAttribute("productos", productoService.listarProductos());
            model.addAttribute("factura", dto);
            return "facturas-proveedor/form";
        }

        var facturaGuardada = facturaProveedorService.registrarFacturaProveedor(dto);
        redirectAttributes.addFlashAttribute("successMessage", "Compra registrada correctamente.");
        return "redirect:/compras/detalle/" + facturaGuardada.getId();
    }

    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable String id, Model model) {
        model.addAttribute("factura", facturaProveedorService.buscarPorId(id));
        return "facturas-proveedor/detalle";
    }
}
