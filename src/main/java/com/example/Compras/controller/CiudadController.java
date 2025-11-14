package com.example.Compras.controller;

import com.example.Compras.dto.CiudadRequestDTO;
import com.example.Compras.dto.CiudadResponseDTO;
import com.example.Compras.services.CiudadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ciudades") // ✅ mejor con / inicial
@Tag(name = "Ciudad", description = "Operaciones CRUD para ciudad")
public class CiudadController {

    private final CiudadService ciudadService;

    public CiudadController(CiudadService ciudadService) {
        this.ciudadService = ciudadService;
    }

    // =========================================================
    // 🟢 Crear una ciudad
    // =========================================================
    @Operation(summary = "Crear una ciudad")
    @PostMapping
    public ResponseEntity<CiudadResponseDTO> crear(@RequestBody CiudadRequestDTO dto) {
        return ResponseEntity.ok(ciudadService.crearCiudad(dto));
    }

    // =========================================================
    // 🟢 Listar todas las ciudades
    // =========================================================
    @Operation(summary = "Listar todas las ciudades")
    @GetMapping
    public ResponseEntity<List<CiudadResponseDTO>> listar() {
        return ResponseEntity.ok(ciudadService.listarCiudades());
    }

    // =========================================================
    // 🟢 Obtener una ciudad por ID
    // =========================================================
    @Operation(summary = "Obtener una ciudad por ID")
    @GetMapping("/{id}")
    public ResponseEntity<CiudadResponseDTO> obtener(@PathVariable Long id) { // ✅ cambiado a Long
        return ResponseEntity.ok(ciudadService.obtenerCiudad(id));
    }

    // =========================================================
    // 🟡 Actualizar una ciudad
    // =========================================================
    @Operation(summary = "Actualizar una ciudad")
    @PutMapping("/{id}")
    public ResponseEntity<CiudadResponseDTO> actualizar(
            @PathVariable Long id, // ✅ cambiado a Long
            @RequestBody CiudadRequestDTO dto) {
        return ResponseEntity.ok(ciudadService.actualizarCiudad(id, dto));
    }

    // =========================================================
    // 🔴 Eliminar una ciudad
    // =========================================================
    @Operation(summary = "Eliminar una ciudad")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) { // ✅ cambiado a Long
        ciudadService.eliminarCiudad(id);
        return ResponseEntity.noContent().build();
    }
}
