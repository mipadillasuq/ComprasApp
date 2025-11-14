package com.example.Compras.services;

import com.example.Compras.dto.ProductoRequestDTO;
import com.example.Compras.entities.Producto;
import com.example.Compras.repositories.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {
    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final MarcaRepository marcaRepository;
    private final UnidadMedidaRepository unidadMedidaRepository;
    private final ImpuestoRepository impuestoRepository;

    public ProductoService(ProductoRepository productoRepository,
                           CategoriaRepository categoriaRepository,
                           MarcaRepository marcaRepository,
                           UnidadMedidaRepository unidadMedidaRepository,
                           ImpuestoRepository impuestoRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.marcaRepository = marcaRepository;
        this.unidadMedidaRepository = unidadMedidaRepository;
        this.impuestoRepository = impuestoRepository;
    }
    public List<Producto> getAll() {
        return productoRepository.findAll();
    }

    public Optional<Producto> getProductoById(Long id) {
        return productoRepository.findById(id);
    }

        public Producto guardar(ProductoRequestDTO dto) {
            Producto producto = new Producto();
            producto.setNombre(dto.getNombre());
            producto.setPrecio(dto.getPrecio());
            producto.setStock(dto.getStock());
            producto.setEstado(dto.getEstado());
            producto.setCantidadUnidadesMedida(dto.getCantidadUnidadesMedida());

            if (dto.getCategoriaId() != null) {
                producto.setCategoria(
                        categoriaRepository.findById(dto.getCategoriaId())
                                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"))
                );
            }

            if (dto.getMarcaId() != null) {
                producto.setMarca(
                        marcaRepository.findById(dto.getMarcaId())
                                .orElseThrow(() -> new RuntimeException("Marca no encontrada"))
                );
            }

            if (dto.getUnidadMedidaId() != null) {
                producto.setUnidadMedida(
                        unidadMedidaRepository.findById(dto.getUnidadMedidaId())
                                .orElseThrow(() -> new RuntimeException("Unidad de medida no encontrada"))
                );
            }

            if (dto.getImpuestoId() != null) {
                producto.setImpuesto(
                        impuestoRepository.findById(dto.getImpuestoId())
                                .orElseThrow(() -> new RuntimeException("Impuesto no encontrado"))
                );
            }

            return productoRepository.save(producto);
        }

    public void eliminar(Long id) {
        productoRepository.deleteById(id);
    }

    public Producto actualizarProducto(Long id, ProductoRequestDTO dto) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Actualizamos solo los campos que vienen en el DTO
        producto.setNombre(dto.getNombre());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        producto.setEstado(dto.getEstado());
        producto.setCantidadUnidadesMedida(dto.getCantidadUnidadesMedida());

        if (dto.getCategoriaId() != null) {
            producto.setCategoria(
                    categoriaRepository.findById(dto.getCategoriaId())
                            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"))
            );
        }

        if (dto.getMarcaId() != null) {
            producto.setMarca(
                    marcaRepository.findById(dto.getMarcaId())
                            .orElseThrow(() -> new RuntimeException("Marca no encontrada"))
            );
        }

        if (dto.getUnidadMedidaId() != null) {
            producto.setUnidadMedida(
                    unidadMedidaRepository.findById(dto.getUnidadMedidaId())
                            .orElseThrow(() -> new RuntimeException("Unidad de medida no encontrada"))
            );
        }

        if (dto.getImpuestoId() != null) {
            producto.setImpuesto(
                    impuestoRepository.findById(dto.getImpuestoId())
                            .orElseThrow(() -> new RuntimeException("Impuesto no encontrado"))
            );
        }

        return productoRepository.save(producto);
    }
}

