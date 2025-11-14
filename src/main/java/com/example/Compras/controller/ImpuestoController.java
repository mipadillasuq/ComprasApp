package com.example.Compras.controller;

import com.example.Compras.dto.ImpuestoRequestDTO;
import com.example.Compras.dto.ImpuestoResponseDTO;
import com.example.Compras.services.ImpuestoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("impuestos")
@Tag(name = "Impuestos", description = "Operaciones CRUD para impuestos")
public class ImpuestoController {

    private final ImpuestoService impuestoService;

    public ImpuestoController(ImpuestoService impuestoService) {
        this.impuestoService = impuestoService;
    }
    @Operation(summary = "Crear un impuesto")
    @PostMapping
    public ResponseEntity<ImpuestoResponseDTO> crear(@Valid @RequestBody ImpuestoRequestDTO request) {
        return ResponseEntity.ok(impuestoService.crearImpuesto(request));
    }

    @Operation(summary = "Actualizar un impuesto")
    @PutMapping("/{id}")
    public ResponseEntity<ImpuestoResponseDTO> actualizar(@PathVariable Long id,
                                                          @Valid @RequestBody ImpuestoRequestDTO request) {
        return ResponseEntity.ok(impuestoService.actualizarImpuesto(id, request));
    }

    @Operation(summary = "Eliminar un impuesto")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        impuestoService.eliminarImpuesto(id);
        return ResponseEntity.noContent().build();
    }
    @Operation(summary = "Listar todos los impuestos")
    @GetMapping
    public ResponseEntity<List<ImpuestoResponseDTO>> listar() {
        return ResponseEntity.ok(impuestoService.listarImpuestos());
    }
    @Operation(summary = "Obtener un impuesto por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ImpuestoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(impuestoService.obtenerImpuestoPorId(id));
    }
}