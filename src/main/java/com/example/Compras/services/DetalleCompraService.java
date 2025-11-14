package com.example.Compras.services;

import com.example.Compras.dto.DetalleCompraRequestDTO;
import com.example.Compras.dto.DetalleCompraResponseDTO;
import com.example.Compras.entities.Compra;
import com.example.Compras.entities.DetalleCompra;
import com.example.Compras.entities.Producto;
import com.example.Compras.repositories.CompraRepository;
import com.example.Compras.repositories.DetalleCompraRepository;
import com.example.Compras.repositories.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DetalleCompraService {

    private final DetalleCompraRepository detalleCompraRepository;
    private final CompraRepository compraRepository;
    private final ProductoRepository productoRepository;

    public DetalleCompraService(
            DetalleCompraRepository detalleCompraRepository,
            CompraRepository compraRepository,
            ProductoRepository productoRepository) {

        this.detalleCompraRepository = detalleCompraRepository;
        this.compraRepository = compraRepository;
        this.productoRepository = productoRepository;
    }

    // ======================================================================
    // 🔵 LISTAR TODOS COMO DTO
    // ======================================================================
    public List<DetalleCompraResponseDTO> getAllDto() {
        return detalleCompraRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // ======================================================================
    // 🔵 BUSCAR UNO POR ID COMO DTO
    // ======================================================================
    public Optional<DetalleCompraResponseDTO> getByIdDto(Long id) {
        return detalleCompraRepository.findById(id)
                .map(this::mapToDto);
    }

    // ======================================================================
    // 🔵 CREAR DESDE DTO
    // ======================================================================
    public DetalleCompra crearDesdeDto(DetalleCompraRequestDTO dto) {

        if (dto.getIdCompra() == null)
            throw new IllegalArgumentException("El id de la compra es obligatorio");

        if (dto.getIdProducto() == null)
            throw new IllegalArgumentException("El id del producto es obligatorio");

        Compra compra = compraRepository.findById(dto.getIdCompra())
                .orElseThrow(() -> new IllegalArgumentException("Compra no encontrada"));

        Producto producto = productoRepository.findById(dto.getIdProducto())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        // 1️⃣ Crear el detalle normalito
        DetalleCompra detalle = new DetalleCompra();
        detalle.setCantidad(dto.getCantidad());
        detalle.setCompra(compra);
        detalle.setProducto(producto);

        DetalleCompra guardado = detalleCompraRepository.save(detalle);

        // 2️⃣ Actualizar inventario del producto
        Integer stockActual = producto.getStock() == null ? 0 : producto.getStock();
        producto.setStock(stockActual + dto.getCantidad());
        productoRepository.save(producto);

        return guardado;
    }


    // ======================================================================
    // 🔵 ELIMINAR DETALLE
    // ======================================================================
    public void eliminar(Long id) {
        DetalleCompra detalle = detalleCompraRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Detalle no encontrado"));

        Producto producto = detalle.getProducto();
        Integer stockActual = producto.getStock() == null ? 0 : producto.getStock();
        producto.setStock(stockActual - detalle.getCantidad());
        productoRepository.save(producto);

        detalleCompraRepository.delete(detalle);
    }


    // ======================================================================
    // 🔵 MAPPER ENTIDAD → DTO
    // ======================================================================
    private DetalleCompraResponseDTO mapToDto(DetalleCompra detalle) {

        DetalleCompraResponseDTO dto = new DetalleCompraResponseDTO();
        dto.setId(detalle.getId()); // <-- campo correcto
        dto.setIdProducto(detalle.getProducto().getId());
        dto.setCantidad(detalle.getCantidad());
        dto.setIdCompra(detalle.getCompra().getId());

        return dto;
    }

}



