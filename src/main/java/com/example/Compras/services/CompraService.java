package com.example.Compras.services;

import com.example.Compras.dto.CompraRequestDTO;
import com.example.Compras.dto.CompraResponseDTO;
import com.example.Compras.entities.Compra;
import com.example.Compras.entities.Proveedor;
import com.example.Compras.entities.Usuario;
import com.example.Compras.repositories.CompraRepository;
import com.example.Compras.repositories.ProveedorRepository;
import com.example.Compras.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompraService {

    private final CompraRepository compraRepository;
    private final ProveedorRepository proveedorRepository;
    private final UsuarioRepository usuarioRepository;

    public CompraService(CompraRepository compraRepository,
                         ProveedorRepository proveedorRepository,
                         UsuarioRepository usuarioRepository) {
        this.compraRepository = compraRepository;
        this.proveedorRepository = proveedorRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // ===========================
    // 💾 CREAR COMPRA
    // ===========================
    @Transactional
    public CompraResponseDTO crearCompra(CompraRequestDTO request) {

        // ✅ Validaciones de negocio
        if (request == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La solicitud está vacía");

        if (request.getNumFactura() == null || request.getNumFactura().trim().isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El número de factura es obligatorio");

        if (request.getIdProveedor() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe indicar un proveedor válido");

        if (request.getIdUsuario() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe indicar un usuario válido");

        // ✅ Verificar factura duplicada
        compraRepository.findByNumFactura(request.getNumFactura())
                .ifPresent(c -> {
                    throw new ResponseStatusException(
                            HttpStatus.CONFLICT,
                            "Ya existe una compra con el número de factura: " + request.getNumFactura()
                    );
                });

        // ✅ Buscar proveedor y usuario
        Proveedor proveedor = proveedorRepository.findById(request.getIdProveedor())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Proveedor no encontrado con ID: " + request.getIdProveedor()));

        Usuario usuario = usuarioRepository.findById(request.getIdUsuario())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Usuario no encontrado con ID: " + request.getIdUsuario()));

        // ✅ Convertir la fecha
        LocalDate fecha;
        try {
            fecha = LocalDate.parse(request.getFecha());
        } catch (DateTimeParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Formato de fecha inválido. Use yyyy-MM-dd");
        }

        // ✅ Crear la entidad y guardar
        Compra compra = new Compra();
        compra.setFecha(fecha);
        compra.setNumFactura(request.getNumFactura().trim());
        compra.setProveedor(proveedor);
        compra.setUsuario(usuario);

        Compra guardada = compraRepository.save(compra);

        // ✅ Retornar DTO
        return mapToResponseDTO(guardada);
    }

    // ===========================
    // 📋 LISTAR TODAS LAS COMPRAS
    // ===========================
    @Transactional
    public List<CompraResponseDTO> getAllCompras() {
        return compraRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // ===========================
    // 🧭 OBTENER POR ID
    // ===========================
    @Transactional
    public CompraResponseDTO getCompraById(Long id) {
        Compra compra = compraRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Compra no encontrada con ID: " + id));
        return mapToResponseDTO(compra);
    }

    // ===========================
    // 🗑️ ELIMINAR COMPRA
    // ===========================

    @Transactional
    public void eliminarCompra(Long id) {
        Compra compra = compraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada con id: " + id));

        compraRepository.delete(compra);
    }



    // ===========================
    // 🔄 MAPEO ENTIDAD → DTO
    // ===========================
    private CompraResponseDTO mapToResponseDTO(Compra compra) {
        CompraResponseDTO dto = new CompraResponseDTO();
        dto.setId(compra.getId());
        dto.setFecha(compra.getFecha());
        dto.setNumFactura(compra.getNumFactura());
        dto.setIdProveedor(compra.getProveedor().getId());
        dto.setIdUsuario(compra.getUsuario().getId());
        return dto;
    }
}
