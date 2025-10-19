package com.example.Compras.services;

import com.example.Compras.dto.CompraRequestDTO;
import com.example.Compras.dto.CompraResponseDTO;
import com.example.Compras.dto.DetalleCompraRequestDTO;
import com.example.Compras.dto.DetalleCompraResponseDTO;
import com.example.Compras.entities.*;
import com.example.Compras.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class CompraService {

    private final CompraRepository compraRepository;
    private final ProveedorRepository proveedorRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final DetalleCompraRepository detalleCompraRepository;

    public CompraService(CompraRepository compraRepository,
                         ProveedorRepository proveedorRepository,
                         UsuarioRepository usuarioRepository,
                         ProductoRepository productoRepository,
                         DetalleCompraRepository detalleCompraRepository) {
        this.compraRepository = compraRepository;
        this.proveedorRepository = proveedorRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
        this.detalleCompraRepository = detalleCompraRepository;
    }

    /**
     * Crea una compra (valida factura única, proveedor y usuario).
     * Si vienen detalles, los guarda vinculados a la compra.
     */
    @Transactional
    public CompraResponseDTO crearCompra(CompraRequestDTO request) {

        // 1. Validar numFactura único
        compraRepository.findByNumFactura(request.getNumFactura()).ifPresent(c -> {
            throw new RuntimeException("Ya existe una compra con el número de factura: " + request.getNumFactura());
        });

        // 2. Validar proveedor
        Proveedor proveedor = proveedorRepository.findById(request.getIdProveedor())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con ID: " + request.getIdProveedor()));

        // 3. Validar usuario
        Usuario usuario = usuarioRepository.findById(request.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + request.getIdUsuario()));

        // 4. Crear compra sin detalles
        Compra compra = new Compra();
        compra.setFecha(request.getFecha());
        compra.setNumFactura(request.getNumFactura());
        compra.setProveedor(proveedor);
        compra.setUsuario(usuario);

        compra = compraRepository.save(compra);

        // 5. Agregar detalles si existen
        if (request.getDetalles() != null && !request.getDetalles().isEmpty()) {
            for (DetalleCompraRequestDTO detReq : request.getDetalles()) {

                Producto producto = productoRepository.findById(detReq.getIdProducto())
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + detReq.getIdProducto()));

                DetalleCompra detalle = new DetalleCompra();
                detalle.setCompra(compra);
                detalle.setProducto(producto);
                detalle.setCantidad(detReq.getCantidad());

                detalleCompraRepository.save(detalle);
            }
            // recargar la lista de detalles en la compra (opcional)
            compra.setDetalles(detalleCompraRepository.findByCompraId(compra.getId()));
        }

        // 6. Devolver DTO de respuesta
        return mapToResponseDTO(compra);
    }

    /**
     * Obtener compra por id
     */
    @Transactional
    public CompraResponseDTO getCompraById(Long id) {
        Compra compra = compraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada con ID: " + id));
        // asegurarnos de cargar detalles si no vienen
        compra.setDetalles(detalleCompraRepository.findByCompraId(compra.getId()));
        return mapToResponseDTO(compra);
    }

    /**
     * Listar todas las compras
     */
    @Transactional
    public List<CompraResponseDTO> getAllCompras() {
        List<Compra> compras = compraRepository.findAll();
        // cargar detalles por cada compra
        compras.forEach(c -> c.setDetalles(detalleCompraRepository.findByCompraId(c.getId())));
        return compras.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }


    @Transactional
    public void eliminarCompra(Long id) {
        Compra compra = compraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada con ID: " + id));
        // eliminar detalles primero (si tu FK no tiene cascade)
        List<DetalleCompra> detalles = detalleCompraRepository.findByCompraId(compra.getId());
        if (detalles != null && !detalles.isEmpty()) {
            detalleCompraRepository.deleteAll(detalles);
        }
        compraRepository.delete(compra);
    }

    /**
     * Mapea entidad Compra -> CompraResponseDTO (incluye detalles)
     */
    private CompraResponseDTO mapToResponseDTO(Compra compra) {
        CompraResponseDTO dto = new CompraResponseDTO();
        dto.setId(compra.getId());
        dto.setFecha(compra.getFecha());
        dto.setNumFactura(compra.getNumFactura());
        dto.setIdProveedor(compra.getProveedor() != null ? compra.getProveedor().getId() : null);
        dto.setIdUsuario(compra.getUsuario() != null ? compra.getUsuario().getId() : null);

        List<DetalleCompraResponseDTO> detalles = compra.getDetalles() != null
                ? compra.getDetalles().stream().map(det -> {
            DetalleCompraResponseDTO d = new DetalleCompraResponseDTO();
            // suponiendo DetalleCompra tiene getId()
            d.setId(det.getIdDetalleCompra());
            d.setIdProducto(det.getProducto() != null ? det.getProducto().getId() : null);
            d.setCantidad(det.getCantidad());
            return d;
        }).collect(Collectors.toList())
                : null;

        dto.setDetalles(detalles);
        return dto;
    }
}