package com.example.Compras.services;

import com.example.Compras.dto.CiudadRequestDTO;
import com.example.Compras.dto.CiudadResponseDTO;
import com.example.Compras.entities.Ciudad;
import com.example.Compras.repositories.CiudadRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CiudadService {

    private final CiudadRepository ciudadRepository;

    public CiudadService(CiudadRepository ciudadRepository) {
        this.ciudadRepository = ciudadRepository;
    }

    // =========================================================
    // 🟢 Crear ciudad
    // =========================================================
    public CiudadResponseDTO crearCiudad(CiudadRequestDTO dto) {
        ciudadRepository.findByNombreIgnoreCase(dto.getNombre())
                .ifPresent(c -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La ciudad ya existe");
                });

        Ciudad ciudad = new Ciudad(dto.getNombre());
        Ciudad guardada = ciudadRepository.save(ciudad);

        return mapToResponse(guardada);
    }

    // =========================================================
    // 🟢 Listar ciudades
    // =========================================================
    public List<CiudadResponseDTO> listarCiudades() {
        return ciudadRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================================================
    // 🟢 Obtener ciudad por ID
    // =========================================================
    public CiudadResponseDTO obtenerCiudad(Long id) { // ✅ cambiado a Long
        Ciudad ciudad = ciudadRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ciudad no encontrada"));

        return mapToResponse(ciudad);
    }

    // =========================================================
    // 🟡 Actualizar ciudad
    // =========================================================
    public CiudadResponseDTO actualizarCiudad(Long id, CiudadRequestDTO dto) { // ✅ Long
        Ciudad ciudad = ciudadRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ciudad no encontrada"));

        ciudadRepository.findByNombreIgnoreCase(dto.getNombre())
                .ifPresent(c -> {
                    if (!c.getId().equals(id)) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Otra ciudad con ese nombre ya existe");
                    }
                });

        ciudad.setNombre(dto.getNombre());
        Ciudad actualizada = ciudadRepository.save(ciudad);

        return mapToResponse(actualizada);
    }

    // =========================================================
    // 🔴 Eliminar ciudad
    // =========================================================
    public void eliminarCiudad(Long id) { // ✅ Long
        if (!ciudadRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ciudad no encontrada");
        }
        ciudadRepository.deleteById(id);
    }

    // =========================================================
    // 🧭 Mapeo a DTO
    // =========================================================
    private CiudadResponseDTO mapToResponse(Ciudad ciudad) {
        CiudadResponseDTO dto = new CiudadResponseDTO();
        dto.setId(ciudad.getId());
        dto.setNombre(ciudad.getNombre());
        return dto;
    }
}

