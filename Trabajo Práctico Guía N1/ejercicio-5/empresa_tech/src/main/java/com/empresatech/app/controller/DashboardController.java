package com.empresatech.app.controller;

import com.empresatech.app.dto.response.ClienteResponseDTO;
import com.empresatech.app.dto.response.FacturaClienteResponseDTO;
import com.empresatech.app.dto.response.ProductoResponseDTO;
import com.empresatech.app.service.ClienteService;
import com.empresatech.app.service.FacturaClienteService;
import com.empresatech.app.service.ProductoService;
import com.empresatech.app.service.ProveedorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Controlador del panel principal (dashboard) del sistema.
 *
 * <p>Su responsabilidad principal es centralizar la información de negocio
 * relevante para el usuario autenticado en una sola vista: catálogo, clientes,
 * ventas y niveles de stock.
 */
@Controller
public class DashboardController {

    private final ProductoService productoService;
    private final ClienteService clienteService;
    private final ProveedorService proveedorService;
    private final FacturaClienteService facturaClienteService;

    public DashboardController(ProductoService productoService,
                               ClienteService clienteService,
                               ProveedorService proveedorService,
                               FacturaClienteService facturaClienteService) {
        this.productoService = productoService;
        this.clienteService = clienteService;
        this.proveedorService = proveedorService;
        this.facturaClienteService = facturaClienteService;
    }

    @GetMapping({"/", "/dashboard"})
    public String index(Model model) {
        List<ProductoResponseDTO> productos = productoService.listarProductos();
        List<ClienteResponseDTO> clientes = clienteService.listarTodos();
        List<FacturaClienteResponseDTO> facturas = facturaClienteService.listarFacturasCliente();

        BigDecimal totalVendidoMes = BigDecimal.ZERO;
        LocalDate hoy = LocalDate.now();

        if (facturas != null) {
            for (FacturaClienteResponseDTO factura : facturas) {
                // Sumar si tiene monto válido
                if (factura.getTotal() != null) {
                    if (factura.getFecha() != null
                            && factura.getFecha().getMonth().equals(hoy.getMonth())
                            && factura.getFecha().getYear() == hoy.getYear()) {
                        totalVendidoMes = totalVendidoMes.add(factura.getTotal());
                    }
                }
            }
        }

        long productosConStockBajo = 0;
        if (productos != null) {
            productosConStockBajo = productos.stream()
                    .filter(producto -> producto.getStock() != null)
                    .filter(producto -> producto.getStock().getCantidad() != null && producto.getStock().getCantidad() <= 5)
                    .count();
        }

        // Métricas enviadas al HTML
        model.addAttribute("totalProductos", productos != null ? productos.size() : 0);
        model.addAttribute("totalClientes", clientes != null ? clientes.size() : 0);
        model.addAttribute("totalProveedores", proveedorService.listarTodos() != null ? proveedorService.listarTodos().size() : 0);

        // Cantidad de facturas emitidas (para que no quede en 0 si busca totalVentas)
        model.addAttribute("totalVentas", facturas != null ? facturas.size() : 0);

        // Monto total acumulado en dinero
        model.addAttribute("totalVendidoMes", totalVendidoMes);
        model.addAttribute("productosConStockBajo", productosConStockBajo);

        return "dashboard/index";
    }
}