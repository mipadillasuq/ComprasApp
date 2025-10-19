package com.example.Compras.controller;

import com.example.Compras.dto.TelefonoRequestDTO;
import com.example.Compras.dto.TelefonoResponseDTO;
import com.example.Compras.services.TelefonoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("telefonos")
@Tag(name = "Teléfono", description = "Operaciones CRUD para teléfonos")
public class TelefonoController {
    private final TelefonoService telefonoService;

    public TelefonoController(TelefonoService telefonoService) {
        this.telefonoService = telefonoService;
    }
    @Operation(summary = "Crear un teléfono")
    @PostMapping
    public ResponseEntity<TelefonoResponseDTO> crear(@Valid @RequestBody TelefonoRequestDTO request) {
        return ResponseEntity.ok(telefonoService.crearTelefono(request));
    }
    @Operation(summary = "Listar todos los teléfonos")
    @GetMapping
    public ResponseEntity<List<TelefonoResponseDTO>> listar() {
        return ResponseEntity.ok(telefonoService.listarTodos());
    }
    @Operation(summary = "Obtener un teléfono por ID")
    @GetMapping("/{id}")
    public ResponseEntity<TelefonoResponseDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(telefonoService.buscarPorId(id));
    }
    @Operation(summary = "Actualizar un teléfono")
    @PutMapping("/{id}")
    public ResponseEntity<TelefonoResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody TelefonoRequestDTO request) {
        return ResponseEntity.ok(telefonoService.actualizarTelefono(id, request));
    }
    @Operation(summary = "Eliminar un teléfono")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        telefonoService.eliminarTelefono(id);
        return ResponseEntity.noContent().build();
    }
}
