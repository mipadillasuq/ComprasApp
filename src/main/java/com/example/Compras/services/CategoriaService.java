package com.example.Compras.services;

import com.example.Compras.dto.CategoriaRequestDTO;
import com.example.Compras.dto.CategoriaResponseDTO;
import com.example.Compras.entities.Categoria;
import com.example.Compras.repositories.CategoriaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public CategoriaResponseDTO crearCategoria(CategoriaRequestDTO dto) {
        categoriaRepository.findByNombreIgnoreCase(dto.getNombre())
                .ifPresent(c -> { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La categoría ya existe"); });

        Categoria categoria = new Categoria();
        categoria.setNombre(dto.getNombre());

        return mapToDTO(categoriaRepository.save(categoria));
    }

    public List<CategoriaResponseDTO> listarCategorias() {
        return categoriaRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public CategoriaResponseDTO actualizarCategoria(Long id, CategoriaRequestDTO dto) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada"));

        categoriaRepository.findByNombreIgnoreCase(dto.getNombre())
                .filter(c -> !c.getId().equals(id))
                .ifPresent(c -> { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Otra categoría con ese nombre ya existe"); });

        categoria.setNombre(dto.getNombre());

        return mapToDTO(categoriaRepository.save(categoria));
    }

    public void eliminarCategoria(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada");
        }
        categoriaRepository.deleteById(id);
    }

    private CategoriaResponseDTO mapToDTO(Categoria categoria) {
        CategoriaResponseDTO dto = new CategoriaResponseDTO();
        dto.setId(categoria.getId());
        dto.setNombre(categoria.getNombre());
        return dto;
    }
}
