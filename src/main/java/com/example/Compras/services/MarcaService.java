package com.example.Compras.services;

import com.example.Compras.dto.MarcaRequestDTO;
import com.example.Compras.dto.MarcaResponseDTO;
import com.example.Compras.entities.Marca;
import com.example.Compras.repositories.MarcaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MarcaService {
    private final MarcaRepository marcaRepository;

    public MarcaService(MarcaRepository marcaRepository) {
        this.marcaRepository = marcaRepository;
    }

    // Crear una nueva marca
    @Transactional
    public MarcaResponseDTO crearMarca(MarcaRequestDTO request) {
        marcaRepository.findByNombreIgnoreCase(request.getNombre())
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Ya existe una marca con el nombre '" + request.getNombre() + "'");
                });

        Marca marca = new Marca();
        marca.setNombre(request.getNombre());

        marcaRepository.save(marca);

        return new MarcaResponseDTO(marca.getId(), marca.getNombre());
    }

    // Actualizar una marca existente
    @Transactional
    public MarcaResponseDTO actualizarMarca(Long id, MarcaRequestDTO request) {
        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la marca con id " + id));

        marcaRepository.findByNombreIgnoreCase(request.getNombre())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Ya existe otra marca con el nombre '" + request.getNombre() + "'");
                });

        marca.setNombre(request.getNombre());
        marcaRepository.save(marca);

        return new MarcaResponseDTO(marca.getId(), marca.getNombre());
    }

    // Eliminar marca
    @Transactional
    public void eliminarMarca(Long id) {
        if (!marcaRepository.existsById(id)) {
            throw new IllegalArgumentException("No se encontró la marca con id " + id);
        }
        marcaRepository.deleteById(id);
    }

    // Listar todas las marcas
    @Transactional(readOnly = true)
    public List<MarcaResponseDTO> listarMarcas() {
        return marcaRepository.findAll().stream()
                .map(m -> new MarcaResponseDTO(m.getId(), m.getNombre()))
                .collect(Collectors.toList());
    }

    // Obtener marca por ID
    @Transactional(readOnly = true)
    public MarcaResponseDTO obtenerMarcaPorId(Long id) {
        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la marca con id " + id));

        return new MarcaResponseDTO(marca.getId(), marca.getNombre());
    }
}
