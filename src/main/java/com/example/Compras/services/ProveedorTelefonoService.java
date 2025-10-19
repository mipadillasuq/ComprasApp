package com.example.Compras.services;
import com.example.Compras.dto.ProveedorResponseDTO;
import com.example.Compras.dto.ProveedorTelefonoRequestDTO;
import com.example.Compras.dto.ProveedorTelefonoResponseDTO;
import com.example.Compras.dto.TelefonoResponseDTO;
import com.example.Compras.entities.Proveedor;
import com.example.Compras.entities.ProveedorTelefono;
import com.example.Compras.entities.Telefono;
import com.example.Compras.repositories.ProveedorRepository;
import com.example.Compras.repositories.ProveedorTelefonoRepository;
import com.example.Compras.repositories.TelefonoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProveedorTelefonoService {
    private final ProveedorTelefonoRepository proveedorTelefonoRepository;
    private final ProveedorRepository proveedorRepository;
    private final TelefonoRepository telefonoRepository;

    public ProveedorTelefonoService(ProveedorTelefonoRepository proveedorTelefonoRepository,
                                    ProveedorRepository proveedorRepository,
                                    TelefonoRepository telefonoRepository) {
        this.proveedorTelefonoRepository = proveedorTelefonoRepository;
        this.proveedorRepository = proveedorRepository;
        this.telefonoRepository = telefonoRepository;
    }

    // Crear una relación proveedor-teléfono
    @Transactional
    public ProveedorTelefonoResponseDTO crearRelacion(ProveedorTelefonoRequestDTO request) {
        Proveedor proveedor = proveedorRepository.findById(request.getIdProveedor())
                .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado"));

        Telefono telefono = telefonoRepository.findById(request.getIdTelefono())
                .orElseThrow(() -> new IllegalArgumentException("Teléfono no encontrado"));

        ProveedorTelefono relacion = new ProveedorTelefono();
        relacion.setProveedor(proveedor);
        relacion.setTelefono(telefono);

        proveedorTelefonoRepository.save(relacion);

        return new ProveedorTelefonoResponseDTO(
                relacion.getId(),
                proveedor.getId(),
                proveedor.getNombre(),
                telefono.getId(),
                telefono.getNumero()
        );
    }

    // Listar teléfonos de un proveedor
    @Transactional(readOnly = true)
    public List<ProveedorTelefonoResponseDTO> listarPorProveedor(Long idProveedor) {
        return proveedorTelefonoRepository.findByProveedorId(idProveedor).stream()
                .map(r -> new ProveedorTelefonoResponseDTO(
                        r.getId(),
                        r.getProveedor().getId(),
                        r.getProveedor().getNombre(),
                        r.getTelefono().getId(),
                        r.getTelefono().getNumero()
                ))
                .collect(Collectors.toList());
    }

    // Eliminar relación
    @Transactional
    public void eliminarRelacion(Long id) {
        if (!proveedorTelefonoRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe una relación con id " + id);
        }
        proveedorTelefonoRepository.deleteById(id);
    }

    public List<ProveedorTelefonoResponseDTO> listarTodos() {
        List<ProveedorTelefono> relaciones = proveedorTelefonoRepository.findAll();

        return relaciones.stream()
                .map(relacion -> new ProveedorTelefonoResponseDTO(
                        relacion.getId(),
                        relacion.getProveedor().getId(),
                        relacion.getProveedor().getNombre(),
                        relacion.getTelefono().getId(),
                        relacion.getTelefono().getNumero()
                ))
                .collect(Collectors.toList());
    }
}
