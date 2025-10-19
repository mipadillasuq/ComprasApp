package com.example.Compras.controller;

import com.example.Compras.dto.UnidadMedidaRequestDTO;
import com.example.Compras.dto.UnidadMedidaResponseDTO;
import com.example.Compras.services.UnidadMedidaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("unidad-medida")
@Tag(name = "Unidad de Medida", description = "Operaciones CRUD para unidades de medida")
public class UnidadMedidaController {
    private final UnidadMedidaService unidadMedidaService;

    public UnidadMedidaController(UnidadMedidaService unidadMedidaService) {
        this.unidadMedidaService = unidadMedidaService;
    }

    @PostMapping
    public ResponseEntity<UnidadMedidaResponseDTO> crearUnidad(@Valid @RequestBody UnidadMedidaRequestDTO request) {
        return ResponseEntity.ok(unidadMedidaService.crearUnidad(request));
    }

    @GetMapping
    public ResponseEntity<List<UnidadMedidaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(unidadMedidaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UnidadMedidaResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(unidadMedidaService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UnidadMedidaResponseDTO> actualizarUnidad(@PathVariable Integer id,
                                                                    @Valid @RequestBody UnidadMedidaRequestDTO request) {
        return ResponseEntity.ok(unidadMedidaService.actualizarUnidad(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUnidad(@PathVariable Integer id) {
        unidadMedidaService.eliminarUnidad(id);
        return ResponseEntity.noContent().build();
    }
}
