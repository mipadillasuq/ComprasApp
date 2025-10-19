package com.example.Compras.controller;

import com.example.Compras.dto.CategoriaRequestDTO;
import com.example.Compras.dto.CategoriaResponseDTO;
import com.example.Compras.services.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("categorias")
@Tag(name = "Categorias", description = "Operaciones CRUD para categorias")
public class CategoriaController {
    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @Operation(summary = "Crear una categoría")
    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> crear(@RequestBody CategoriaRequestDTO dto) {
        return ResponseEntity.ok(categoriaService.crearCategoria(dto));
    }
    @Operation(summary = "Listar todas las categorías")
    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> listar() {
        return ResponseEntity.ok(categoriaService.listarCategorias());
    }
    @Operation(summary = "Actualizar una categoría")
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> actualizar(@PathVariable Long id, @RequestBody CategoriaRequestDTO dto) {
        return ResponseEntity.ok(categoriaService.actualizarCategoria(id, dto));
    }

    @Operation(summary = "Eliminar una categría")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        categoriaService.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }
}
