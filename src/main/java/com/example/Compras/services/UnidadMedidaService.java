package com.example.Compras.services;

import com.example.Compras.dto.UnidadMedidaRequestDTO;
import com.example.Compras.dto.UnidadMedidaResponseDTO;
import com.example.Compras.entities.UnidadMedida;
import com.example.Compras.repositories.UnidadMedidaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UnidadMedidaService {
    private final UnidadMedidaRepository unidadMedidaRepository;

    public UnidadMedidaService(UnidadMedidaRepository unidadMedidaRepository) {
        this.unidadMedidaRepository = unidadMedidaRepository;
    }

    // Crear una nueva unidad de medida
    @Transactional
    public UnidadMedidaResponseDTO crearUnidad(UnidadMedidaRequestDTO request) {
        unidadMedidaRepository.findByNombreIgnoreCase(request.getNombre())
                .ifPresent(u -> {
                    throw new IllegalArgumentException("Ya existe una unidad de medida con ese nombre");
                });

        UnidadMedida unidad = new UnidadMedida();
        unidad.setNombre(request.getNombre());
        UnidadMedida guardada = unidadMedidaRepository.save(unidad);

        return new UnidadMedidaResponseDTO(guardada.getId(), guardada.getNombre());
    }

    // Listar todas las unidades
    @Transactional(readOnly = true)
    public List<UnidadMedidaResponseDTO> listarTodas() {
        return unidadMedidaRepository.findAll()
                .stream()
                .map(u -> new UnidadMedidaResponseDTO(u.getId(), u.getNombre()))
                .collect(Collectors.toList());
    }

    // Buscar por ID
    @Transactional(readOnly = true)
    public UnidadMedidaResponseDTO buscarPorId(Integer id) {
        UnidadMedida unidad = unidadMedidaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la unidad con id " + id));
        return new UnidadMedidaResponseDTO(unidad.getId(), unidad.getNombre());
    }

    // Actualizar unidad
    @Transactional
    public UnidadMedidaResponseDTO actualizarUnidad(Integer id, UnidadMedidaRequestDTO request) {
        UnidadMedida unidad = unidadMedidaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la unidad con id " + id));

        unidadMedidaRepository.findByNombreIgnoreCase(request.getNombre())
                .filter(u -> !u.getId().equals(id))
                .ifPresent(u -> {
                    throw new IllegalArgumentException("Ya existe otra unidad con ese nombre");
                });

        unidad.setNombre(request.getNombre());
        unidadMedidaRepository.save(unidad);

        return new UnidadMedidaResponseDTO(unidad.getId(), unidad.getNombre());
    }

    // Eliminar unidad
    @Transactional
    public void eliminarUnidad(Integer id) {
        if (!unidadMedidaRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe una unidad con id " + id);
        }
        unidadMedidaRepository.deleteById(id);
    }
}
