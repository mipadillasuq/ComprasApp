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
@RequestMapping("ciudades")
@Tag(name = "Ciudad", description = "Operaciones CRUD para ciudad")
public class CiudadController {

    private final CiudadService ciudadService;

    public CiudadController(CiudadService ciudadService) {
        this.ciudadService = ciudadService;
    }

    @Operation(summary = "Crear una ciudad")
    @PostMapping
    public ResponseEntity<CiudadResponseDTO> crear(@RequestBody CiudadRequestDTO dto) {
        return ResponseEntity.ok(ciudadService.crearCiudad(dto));
    }

    @Operation(summary = "Listar todas las ciudades")
    @GetMapping
    public ResponseEntity<List<CiudadResponseDTO>> listar() {
        return ResponseEntity.ok(ciudadService.listarCiudades());
    }
    @Operation(summary = "Obtener una ciudad por ID")
    @GetMapping("/{id}")
    public ResponseEntity<CiudadResponseDTO> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(ciudadService.obtenerCiudad(id));
    }

    @Operation(summary = "Actualizar una ciudad")
    @PutMapping("/{id}")
    public ResponseEntity<CiudadResponseDTO> actualizar(@PathVariable Integer id, @RequestBody CiudadRequestDTO dto) {
        return ResponseEntity.ok(ciudadService.actualizarCiudad(id, dto));
    }

    @Operation(summary = "Eliminar una ciudad")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        ciudadService.eliminarCiudad(id);
        return ResponseEntity.noContent().build();
    }
}