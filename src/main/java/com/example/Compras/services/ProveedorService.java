package com.example.Compras.services;

import com.example.Compras.dto.ProveedorRequestDTO;
import com.example.Compras.dto.ProveedorResponseDTO;
import com.example.Compras.dto.TelefonoRequestDTO;
import com.example.Compras.entities.ProveedorTelefono;
import com.example.Compras.entities.Telefono;
import com.example.Compras.repositories.CompraRepository;
import com.example.Compras.repositories.ProveedorRepository;
import com.example.Compras.entities.Proveedor;
import com.example.Compras.repositories.ProveedorTelefonoRepository;
import com.example.Compras.repositories.TelefonoRepository;
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
    @Autowired private CompraRepository compraRepository; // para validar compras existentes

    public ProveedorResponseDTO crearProveedor(ProveedorRequestDTO dto) {
        if (proveedorRepository.existsById(dto.getIdProveedor())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Proveedor ya existe con ese id");
        }

        Proveedor p = new Proveedor();
        p.setId(dto.getIdProveedor());
        p.setNombre(dto.getNombre());
        p.setCiudadId(dto.getCiudadId());
        p.setDireccion(dto.getDireccion());
        p.setEmail(dto.getEmail());
        p.setEstado(dto.getEstado() == null ? true : dto.getEstado());

        Proveedor saved = proveedorRepository.save(p);
        return mapToResponse(saved);
    }

    public ProveedorResponseDTO getProveedor(Long idProveedor) {
        Proveedor p = proveedorRepository.findById(idProveedor)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado"));
        return mapToResponse(p);
    }

    public List<ProveedorResponseDTO> listarTodos() {
        return proveedorRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional
    public void eliminarProveedor(Long idProveedor) {
        Proveedor p = proveedorRepository.findById(idProveedor)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado"));

        // Regla: no eliminar si tiene compras asociadas
        boolean tieneCompras = compraRepository.findAll()
                .stream()
                .anyMatch(c -> c.getProveedor() != null && idProveedor.equals(c.getProveedor().getId()));
        if (tieneCompras) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede eliminar proveedor con compras asociadas");
        }

        // borrar relaciones telefonos primero (por cascada podría no hacer falta)
        List<ProveedorTelefono> rels = proveedorTelefonoRepository.findByProveedorId(idProveedor);
        proveedorTelefonoRepository.deleteAll(rels);

        proveedorRepository.delete(p);
    }

    @Transactional
    public ProveedorResponseDTO agregarTelefono(Long idProveedor, TelefonoRequestDTO telDto) {
        Proveedor p = proveedorRepository.findById(idProveedor)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado"));

        // buscar telefono existente por numero
        Optional<Telefono> optTel = telefonoRepository.findByNumero(telDto.getNumero());
        Telefono telefono = optTel.orElseGet(() -> {
            Telefono t = new Telefono();
            t.setNumero(telDto.getNumero());
            return telefonoRepository.save(t);
        });

        // Evitar duplicado de relacion
        boolean existeRelacion = proveedorTelefonoRepository.existsByProveedorIdAndTelefonoId(idProveedor, telefono.getId());
        if (existeRelacion) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Teléfono ya asociado a este proveedor");
        }

        ProveedorTelefono rel = new ProveedorTelefono();
        rel.setProveedor(p);
        rel.setTelefono(telefono);
        proveedorTelefonoRepository.save(rel);

        // recargar proveedor y devolver
        return mapToResponse(p);
    }

    @Transactional
    public ProveedorResponseDTO quitarTelefono(Long idProveedor, Long idTelefono) {
        Proveedor p = proveedorRepository.findById(idProveedor)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado"));

        // buscar relación
        List<ProveedorTelefono> rels = proveedorTelefonoRepository.findByProveedorId(idProveedor)
                .stream().filter(r -> r.getTelefono().getId().equals(idTelefono)).collect(Collectors.toList());
        if (rels.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Teléfono no asociado al proveedor");
        }

        proveedorTelefonoRepository.deleteAll(rels);

        // opcional: si teléfono no pertenece a nadie más, eliminar teléfono de la tabla
        boolean telefonoUsado = proveedorTelefonoRepository.findAll()
                .stream()
                .anyMatch(r -> r.getTelefono().getId().equals(idTelefono));
        if (!telefonoUsado) {
            telefonoRepository.findById(idTelefono).ifPresent(telefonoRepository::delete);
        }

        return mapToResponse(p);
    }

    // Mapeo simple a DTO (números de teléfono)
    private ProveedorResponseDTO mapToResponse(Proveedor p) {
        ProveedorResponseDTO r = new ProveedorResponseDTO();
        r.setIdProveedor(p.getId());
        r.setNombre(p.getNombre());
        r.setCiudadId(p.getCiudadId());
        r.setDireccion(p.getDireccion());
        r.setEmail(p.getEmail());
        r.setEstado(p.getEstado());

        List<String> telefonos = proveedorTelefonoRepository.findByProveedorId(p.getId())
                .stream()
                .map(rel -> rel.getTelefono().getNumero())
                .collect(Collectors.toList());
        r.setTelefonos(telefonos);
        return r;
    }
}
