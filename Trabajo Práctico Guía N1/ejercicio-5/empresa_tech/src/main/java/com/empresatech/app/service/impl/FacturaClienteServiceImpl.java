package com.empresatech.app.service.impl;

import com.empresatech.app.dto.request.DetalleRequestDTO;
import com.empresatech.app.dto.request.FacturaClienteRequestDTO;
import com.empresatech.app.dto.response.FacturaClienteResponseDTO;
import com.empresatech.app.mapper.DetalleMapper;
import com.empresatech.app.mapper.FacturaClienteMapper;
import com.empresatech.app.model.Cliente;
import com.empresatech.app.model.Detalle;
import com.empresatech.app.model.FacturaCliente;
import com.empresatech.app.model.Producto;
import com.empresatech.app.repository.ClienteRepository;
import com.empresatech.app.repository.FacturaClienteRepository;
import com.empresatech.app.repository.ProductoRepository;
import com.empresatech.app.exception.ResourceNotFoundException;
import com.empresatech.app.service.FacturaClienteService;
import com.empresatech.app.service.StockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * Implementación de la facturación de ventas.
 */
@Service
@Transactional
public class FacturaClienteServiceImpl implements FacturaClienteService {

    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final FacturaClienteRepository facturaClienteRepository;
    private final FacturaClienteMapper facturaClienteMapper;
    private final DetalleMapper detalleMapper;
    private final StockService stockService;

    public FacturaClienteServiceImpl(ClienteRepository clienteRepository,
                                    ProductoRepository productoRepository,
                                    FacturaClienteRepository facturaClienteRepository,
                                    FacturaClienteMapper facturaClienteMapper,
                                    DetalleMapper detalleMapper,
                                    StockService stockService) {
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
        this.facturaClienteRepository = facturaClienteRepository;
        this.facturaClienteMapper = facturaClienteMapper;
        this.detalleMapper = detalleMapper;
        this.stockService = stockService;
    }

    @Override
    public FacturaClienteResponseDTO registrarFacturaCliente(FacturaClienteRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getIdCliente())
                .filter(item -> !item.isEliminado())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + dto.getIdCliente()));

        BigDecimal total = BigDecimal.ZERO;
        for (DetalleRequestDTO detalleRequest : dto.getDetalles()) {
            Producto producto = productoRepository.findById(detalleRequest.getIdProducto())
                    .filter(item -> !item.isEliminado())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + detalleRequest.getIdProducto()));

            stockService.disminuirStock(String.valueOf(producto.getId()), detalleRequest.getCantidad());
            total = total.add(producto.getPrecioUnitario().multiply(BigDecimal.valueOf(detalleRequest.getCantidad())));
        }

        String nroFactura = generarNumeroFacturaCliente();
        FacturaCliente factura = facturaClienteMapper.crearFacturaCliente(nroFactura, cliente, total);
        FacturaCliente guardada = facturaClienteRepository.save(factura);

        for (DetalleRequestDTO detalleRequest : dto.getDetalles()) {
            Producto producto = productoRepository.findById(detalleRequest.getIdProducto())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + detalleRequest.getIdProducto()));

            Detalle detalle = detalleMapper.toEntity(detalleRequest, producto, guardada);
            guardada.getDetalles().add(detalle);
        }

        facturaClienteRepository.save(guardada);
        return facturaClienteMapper.toResponseDTO(guardada, detalleMapper);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FacturaClienteResponseDTO> listarFacturasCliente() {
        return facturaClienteRepository.findAll()
                .stream()
                .filter(item -> !item.isEliminado())
                .map(factura -> facturaClienteMapper.toResponseDTO(factura, detalleMapper))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public FacturaClienteResponseDTO buscarPorId(String id) {
        Long facturaId = parseId(id, "factura cliente");
        FacturaCliente factura = facturaClienteRepository.findById(facturaId)
                .filter(item -> !item.isEliminado())
                .orElseThrow(() -> new ResourceNotFoundException("Factura de cliente no encontrada con id: " + id));

        return facturaClienteMapper.toResponseDTO(factura, detalleMapper);
    }

    private String generarNumeroFacturaCliente() {
        String fecha = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        return "FC-" + fecha + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private Long parseId(String rawId, String nombreEntidad) {
        try {
            return Long.valueOf(rawId);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("El id de " + nombreEntidad + " es inválido: " + rawId, ex);
        }
    }
}
