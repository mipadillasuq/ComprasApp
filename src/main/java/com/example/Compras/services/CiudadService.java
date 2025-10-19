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

    public CiudadResponseDTO crearCiudad(CiudadRequestDTO dto) {
        ciudadRepository.findByNombreIgnoreCase(dto.getNombre())
                .ifPresent(c -> { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La ciudad ya existe"); });

        Ciudad ciudad = new Ciudad(dto.getNombre());
        ciudadRepository.save(ciudad);
        return mapToResponse(ciudad);
    }

    public List<CiudadResponseDTO> listarCiudades() {
        return ciudadRepository.findAll()
                .stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public CiudadResponseDTO obtenerCiudad(Integer id) {
        Ciudad ciudad = ciudadRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ciudad no encontrada"));
        return mapToResponse(ciudad);
    }

    public CiudadResponseDTO actualizarCiudad(Integer id, CiudadRequestDTO dto) {
        Ciudad ciudad = ciudadRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ciudad no encontrada"));

        ciudadRepository.findByNombreIgnoreCase(dto.getNombre())
                .ifPresent(c -> {
                    if (!c.getId().equals(id)) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Otra ciudad con ese nombre ya existe");
                    }
                });

        ciudad.setNombre(dto.getNombre());
        ciudadRepository.save(ciudad);
        return mapToResponse(ciudad);
    }

    public void eliminarCiudad(Integer id) {
        if (!ciudadRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ciudad no encontrada");
        }
        ciudadRepository.deleteById(id);
    }

    private CiudadResponseDTO mapToResponse(Ciudad ciudad) {
        CiudadResponseDTO dto = new CiudadResponseDTO();
        dto.setId(ciudad.getId());
        dto.setNombre(ciudad.getNombre());
        return dto;
    }
}
