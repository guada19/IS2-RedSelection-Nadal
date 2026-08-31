package com.empresatech.app.service.impl;

import com.empresatech.app.dto.request.DetalleRequestDTO;
import com.empresatech.app.dto.request.FacturaProveedorRequestDTO;
import com.empresatech.app.dto.response.FacturaProveedorResponseDTO;
import com.empresatech.app.mapper.DetalleMapper;
import com.empresatech.app.mapper.FacturaProveedorMapper;
import com.empresatech.app.model.Detalle;
import com.empresatech.app.model.FacturaProveedor;
import com.empresatech.app.model.Producto;
import com.empresatech.app.model.Proveedor;
import com.empresatech.app.repository.FacturaProveedorRepository;
import com.empresatech.app.repository.ProductoRepository;
import com.empresatech.app.repository.ProveedorRepository;
import com.empresatech.app.exception.ResourceNotFoundException;
import com.empresatech.app.service.FacturaProveedorService;
import com.empresatech.app.service.StockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * Implementación de la facturación de compras a proveedores.
 */
@Service
@Transactional
public class FacturaProveedorServiceImpl implements FacturaProveedorService {

    private final ProveedorRepository proveedorRepository;
    private final ProductoRepository productoRepository;
    private final FacturaProveedorRepository facturaProveedorRepository;
    private final FacturaProveedorMapper facturaProveedorMapper;
    private final DetalleMapper detalleMapper;
    private final StockService stockService;

    public FacturaProveedorServiceImpl(ProveedorRepository proveedorRepository,
                                      ProductoRepository productoRepository,
                                      FacturaProveedorRepository facturaProveedorRepository,
                                      FacturaProveedorMapper facturaProveedorMapper,
                                      DetalleMapper detalleMapper,
                                      StockService stockService) {
        this.proveedorRepository = proveedorRepository;
        this.productoRepository = productoRepository;
        this.facturaProveedorRepository = facturaProveedorRepository;
        this.facturaProveedorMapper = facturaProveedorMapper;
        this.detalleMapper = detalleMapper;
        this.stockService = stockService;
    }

    @Override
    public FacturaProveedorResponseDTO registrarFacturaProveedor(FacturaProveedorRequestDTO dto) {
        Proveedor proveedor = proveedorRepository.findById(dto.getIdProveedor())
                .filter(item -> item.isActivo())
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con id: " + dto.getIdProveedor()));

        BigDecimal total = BigDecimal.ZERO;
        for (DetalleRequestDTO detalleRequest : dto.getDetalles()) {
            Producto producto = productoRepository.findById(detalleRequest.getIdProducto())
                    .filter(item -> !item.isEliminado())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + detalleRequest.getIdProducto()));

            stockService.incrementarStock(String.valueOf(producto.getId()), detalleRequest.getCantidad());
            total = total.add(producto.getPrecioUnitario().multiply(BigDecimal.valueOf(detalleRequest.getCantidad())));
        }

        String nroFactura = generarNumeroFacturaProveedor();
        FacturaProveedor factura = facturaProveedorMapper.crearFacturaProveedor(nroFactura, proveedor, total);
        FacturaProveedor guardada = facturaProveedorRepository.save(factura);

        for (DetalleRequestDTO detalleRequest : dto.getDetalles()) {
            Producto producto = productoRepository.findById(detalleRequest.getIdProducto())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + detalleRequest.getIdProducto()));

            Detalle detalle = detalleMapper.toEntity(detalleRequest, producto, guardada);
            guardada.getDetalles().add(detalle);
        }

        facturaProveedorRepository.save(guardada);
        return facturaProveedorMapper.toResponseDTO(guardada, detalleMapper);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FacturaProveedorResponseDTO> listarFacturasProveedor() {
        return facturaProveedorRepository.findAll()
                .stream()
                .filter(item -> !item.isEliminado())
                .map(factura -> facturaProveedorMapper.toResponseDTO(factura, detalleMapper))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public FacturaProveedorResponseDTO buscarPorId(String id) {
        Long facturaId = parseId(id, "factura proveedor");
        FacturaProveedor factura = facturaProveedorRepository.findById(facturaId)
                .filter(item -> !item.isEliminado())
                .orElseThrow(() -> new ResourceNotFoundException("Factura de proveedor no encontrada con id: " + id));

        return facturaProveedorMapper.toResponseDTO(factura, detalleMapper);
    }

    private String generarNumeroFacturaProveedor() {
        String fecha = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        return "FP-" + fecha + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private Long parseId(String rawId, String nombreEntidad) {
        try {
            return Long.valueOf(rawId);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("El id de " + nombreEntidad + " es inválido: " + rawId, ex);
        }
    }
}
