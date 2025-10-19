package com.example.Compras.services;

import com.example.Compras.dto.TelefonoRequestDTO;
import com.example.Compras.dto.TelefonoResponseDTO;
import com.example.Compras.entities.Telefono;
import com.example.Compras.repositories.TelefonoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TelefonoService {

    private final TelefonoRepository telefonoRepository;

    public TelefonoService(TelefonoRepository telefonoRepository) {
        this.telefonoRepository = telefonoRepository;
    }

    // Crear teléfono
    @Transactional
    public TelefonoResponseDTO crearTelefono(TelefonoRequestDTO request) {

        // Verifica si ya existe un teléfono con el mismo número
        telefonoRepository.findByNumero(request.getNumero())
                .ifPresent(t -> {
                    throw new IllegalArgumentException("Ya existe un teléfono con ese número");
                });

        Telefono telefono = new Telefono();
        telefono.setNumero(request.getNumero());

        Telefono guardado = telefonoRepository.save(telefono);
        return new TelefonoResponseDTO(guardado.getId(), guardado.getNumero());
    }

    // Listar todos
    @Transactional(readOnly = true)
    public List<TelefonoResponseDTO> listarTodos() {
        return telefonoRepository.findAll()
                .stream()
                .map(t -> new TelefonoResponseDTO(t.getId(), t.getNumero()))
                .collect(Collectors.toList());
    }

    // Buscar por ID
    @Transactional(readOnly = true)
    public TelefonoResponseDTO buscarPorId(Long id) {
        Telefono telefono = telefonoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el teléfono con id " + id));

        return new TelefonoResponseDTO(telefono.getId(), telefono.getNumero());
    }

    // Actualizar teléfono
    @Transactional
    public TelefonoResponseDTO actualizarTelefono(Long id, TelefonoRequestDTO request) {
        Telefono telefono = telefonoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el teléfono con id " + id));

        telefonoRepository.findByNumero(request.getNumero())
                .filter(t -> !t.getId().equals(id)) // evita conflicto consigo mismo
                .ifPresent(t -> {
                    throw new IllegalArgumentException("Ya existe otro teléfono con ese número");
                });

        telefono.setNumero(request.getNumero());
        telefonoRepository.save(telefono);

        return new TelefonoResponseDTO(telefono.getId(), telefono.getNumero());
    }

    // Eliminar teléfono
    @Transactional
    public void eliminarTelefono(Long id) {
        if (!telefonoRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe un teléfono con id " + id);
        }
        telefonoRepository.deleteById(id);
    }
}
