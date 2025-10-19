package com.example.Compras.controller;

import com.example.Compras.dto.CompraRequestDTO;
import com.example.Compras.dto.CompraResponseDTO;
import com.example.Compras.services.CompraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("compras")
@Tag(name = "Compras", description = "Operaciones CRUD para las compras")
public class CompraController {

    private final CompraService compraService;

    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    @Operation(summary = "Obtener una compra por ID")
    @GetMapping("/{id}")
    public ResponseEntity<CompraResponseDTO> getCompra(@PathVariable Long id) {
        CompraResponseDTO compra = compraService.getCompraById(id);
        return ResponseEntity.ok(compra);
    }

    @Operation(summary = "Listar todas las compras")
    @GetMapping()
    public List<CompraResponseDTO> getAll() {
        return compraService.getAllCompras();
    }

    @Operation(summary = "Crear una nueva compra")
    @PostMapping()
    public ResponseEntity<CompraResponseDTO> save(@RequestBody CompraRequestDTO compra) {
        return ResponseEntity.ok(compraService.crearCompra(compra));
    }

    @Operation(summary = "Eliminar una compra")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        compraService.eliminarCompra(id);
        return ResponseEntity.noContent().build();
    }
}


