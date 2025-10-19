package com.example.Compras.controller;

import com.example.Compras.dto.MarcaRequestDTO;
import com.example.Compras.dto.MarcaResponseDTO;
import com.example.Compras.services.MarcaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("marcas")
@Tag(name = "Marcas", description = "Operaciones CRUD para marcas")
public class MarcaController {
    private final MarcaService marcaService;

    public MarcaController(MarcaService marcaService) {
        this.marcaService = marcaService;
    }

    @Operation(summary = "Crear una nueva marca")
    @PostMapping
    public ResponseEntity<MarcaResponseDTO> crear(@Valid @RequestBody MarcaRequestDTO request) {
        return ResponseEntity.ok(marcaService.crearMarca(request));
    }

    @Operation(summary = "Actualizar una marca")
    @PutMapping("/{id}")
    public ResponseEntity<MarcaResponseDTO> actualizar(@PathVariable Long id,
                                                       @Valid @RequestBody MarcaRequestDTO request) {
        return ResponseEntity.ok(marcaService.actualizarMarca(id, request));
    }

    @Operation(summary = "Eliminar una marca")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        marcaService.eliminarMarca(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar todas las marcas")
    @GetMapping
    public ResponseEntity<List<MarcaResponseDTO>> listar() {
        return ResponseEntity.ok(marcaService.listarMarcas());
    }

    @Operation(summary = "Obetener una marca por ID")
    @GetMapping("/{id}")
    public ResponseEntity<MarcaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(marcaService.obtenerMarcaPorId(id));
    }
}
