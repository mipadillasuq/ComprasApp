package com.example.Compras.controller;

import com.example.Compras.entities.DetalleCompra;
import com.example.Compras.services.DetalleCompraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("detalle_compra")
@Tag(name = "Detalles Compras", description = "Operaciones CRUD para detalles compras")
public class DetalleCompraController {

    private final DetalleCompraService detalleCompraService;

    public DetalleCompraController(DetalleCompraService detalleCompraService) {
        this.detalleCompraService = detalleCompraService;
    }


    @Operation(summary = "Listar todos los detalle compra")
    @GetMapping
    public List<DetalleCompra> listar() {
        return detalleCompraService.getAll();
    }

    @Operation(summary = "Obtener un detalle compra por ID")
    @GetMapping("/{id}")
    public ResponseEntity<DetalleCompra> obtenerPorId(@PathVariable Long id) {
        return detalleCompraService.getDetalleCompraById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @Operation(summary = "Crear un detalle compra")
    @PostMapping
    public DetalleCompra crear(@RequestBody DetalleCompra detalleCompra) {
        return detalleCompraService.save(detalleCompra);
    }

    @Operation(summary = "Eliminar un detalle compra")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (detalleCompraService.getDetalleCompraById(id).isPresent()) {
            detalleCompraService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
