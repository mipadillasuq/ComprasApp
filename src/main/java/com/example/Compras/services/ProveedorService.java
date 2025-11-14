package com.example.Compras.services;

import com.example.Compras.dto.ProveedorRequestDTO;
import com.example.Compras.dto.ProveedorResponseDTO;
import com.example.Compras.dto.TelefonoRequestDTO;
import com.example.Compras.entities.Ciudad;
import com.example.Compras.entities.ProveedorTelefono;
import com.example.Compras.entities.Telefono;
import com.example.Compras.repositories.*;
import com.example.Compras.entities.Proveedor;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;
    @Autowired private TelefonoRepository telefonoRepository;
    @Autowired private ProveedorTelefonoRepository proveedorTelefonoRepository;
    @Autowired private CompraRepository compraRepository;
    @Autowired private CiudadRepository ciudadRepository;

    // =========================================================
    // 🟢 Crear nuevo proveedor
    // =========================================================
    public ProveedorResponseDTO crearProveedor(ProveedorRequestDTO dto) {

        Ciudad ciudad = ciudadRepository.findById(dto.getCiudadId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ciudad no encontrada"));

        Proveedor p = new Proveedor();
        p.setNombre(dto.getNombre());
        p.setDireccion(dto.getDireccion());
        p.setEmail(dto.getEmail());
        p.setEstado(dto.getEstado() != null ? dto.getEstado() : true);
        p.setCiudad(ciudad);

        Proveedor saved = proveedorRepository.save(p);
        return mapToResponse(saved);
    }

    // =========================================================
    // 🟢 Obtener proveedor por ID
    // =========================================================
    public ProveedorResponseDTO getProveedor(Long id) {
        Proveedor p = proveedorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado"));
        return mapToResponse(p);
    }

    // =========================================================
    // 🟢 Listar todos los proveedores
    // =========================================================
    public List<ProveedorResponseDTO> listarTodos() {
        return proveedorRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================================================
    // 🟡 Eliminar proveedor
    // =========================================================
    @Transactional
    public void eliminarProveedor(Long idProveedor) {
        Proveedor p = proveedorRepository.findById(idProveedor)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado"));

        // Validar si tiene compras asociadas
        boolean tieneCompras = compraRepository.findAll()
                .stream()
                .anyMatch(c -> c.getProveedor() != null && idProveedor.equals(c.getProveedor().getId()));

        if (tieneCompras) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "No se puede eliminar un proveedor con compras asociadas.");
        }

        // Eliminar relaciones con teléfonos (si existen)
        List<ProveedorTelefono> rels = proveedorTelefonoRepository.findByProveedorId(idProveedor);
        if (!rels.isEmpty()) {
            proveedorTelefonoRepository.deleteAll(rels);
        }

        proveedorRepository.delete(p);
    }

    // =========================================================
    // 🟢 Agregar teléfono a proveedor
    // =========================================================
    @Transactional
    public ProveedorResponseDTO agregarTelefono(Long idProveedor, TelefonoRequestDTO telDto) {
        Proveedor p = proveedorRepository.findById(idProveedor)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado"));

        Telefono telefono = telefonoRepository.findByNumero(telDto.getNumero())
                .orElseGet(() -> {
                    Telefono nuevo = new Telefono();
                    nuevo.setNumero(telDto.getNumero());
                    return telefonoRepository.save(nuevo);
                });

        boolean existeRelacion = proveedorTelefonoRepository.existsByProveedorIdAndTelefonoId(idProveedor, telefono.getId());
        if (existeRelacion) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Teléfono ya asociado a este proveedor.");
        }

        ProveedorTelefono rel = new ProveedorTelefono();
        rel.setProveedor(p);
        rel.setTelefono(telefono);
        proveedorTelefonoRepository.save(rel);

        return mapToResponse(p);
    }

    // =========================================================
    // 🟡 Quitar teléfono
    // =========================================================
    @Transactional
    public ProveedorResponseDTO quitarTelefono(Long idProveedor, Long idTelefono) {
        Proveedor p = proveedorRepository.findById(idProveedor)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado"));

        List<ProveedorTelefono> rels = proveedorTelefonoRepository.findByProveedorId(idProveedor)
                .stream()
                .filter(r -> r.getTelefono().getId().equals(idTelefono))
                .collect(Collectors.toList());

        if (rels.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Teléfono no asociado al proveedor.");
        }

        proveedorTelefonoRepository.deleteAll(rels);

        boolean telefonoUsado = proveedorTelefonoRepository.findAll()
                .stream()
                .anyMatch(r -> r.getTelefono().getId().equals(idTelefono));

        if (!telefonoUsado) {
            telefonoRepository.findById(idTelefono).ifPresent(telefonoRepository::delete);
        }

        return mapToResponse(p);
    }

    // =========================================================
    // 🟢 Actualizar proveedor
    // =========================================================
    @Transactional
    public ProveedorResponseDTO actualizarProveedor(Long id, ProveedorRequestDTO dto) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado"));

        proveedor.setNombre(dto.getNombre());
        proveedor.setDireccion(dto.getDireccion());
        proveedor.setEmail(dto.getEmail());
        proveedor.setEstado(dto.getEstado());

        if (dto.getCiudadId() != null) {
            Ciudad ciudad = ciudadRepository.findById(dto.getCiudadId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ciudad no encontrada"));
            proveedor.setCiudad(ciudad);
        }

        Proveedor actualizado = proveedorRepository.save(proveedor);
        return mapToResponse(actualizado);
    }

    // =========================================================
    // 🧭 Mapeo entidad -> DTO
    // =========================================================
    private ProveedorResponseDTO mapToResponse(Proveedor p) {
        ProveedorResponseDTO r = new ProveedorResponseDTO();
        r.setId(p.getId());
        r.setNombre(p.getNombre());
        r.setDireccion(p.getDireccion());
        r.setEmail(p.getEmail());
        r.setEstado(p.getEstado());

        if (p.getCiudad() != null) {
            r.setCiudadId(p.getCiudad().getId());
            r.setCiudadNombre(p.getCiudad().getNombre());
        }

        List<String> telefonos = proveedorTelefonoRepository.findByProveedorId(p.getId())
                .stream()
                .map(rel -> rel.getTelefono().getNumero())
                .collect(Collectors.toList());
        r.setTelefonos(telefonos);

        return r;
    }
}

