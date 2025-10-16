package com.example.Compras.Controller;

import com.example.Compras.Services.CompraService;
import com.example.Compras.Entities.Compra;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("compras")
@Tag(name = "Compras", description = "Operaciones CRUD para las compras")
public class CompraController {
    private CompraService compraService;

    public CompraController (CompraService compraService) { this.compraService = compraService; }

    @Operation(summary = "Obtener una compra por ID")
    @GetMapping("/{id}")
    public Optional <Compra> getCompra(@PathVariable Long id){return compraService.getCompraById(id); }

    @Operation(summary = "Listar todas las compras")
    @GetMapping()
    public List <Compra> getAll() {return compraService.getAll(); }

    @Operation(summary = "Crear una nueva compra")
    @PostMapping()
    public Compra save (@RequestBody Compra compra) {return compraService.save(compra); }

    @Operation(summary = "Eliminar una compra")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (compraService.getCompraById(id).isPresent()) {
            compraService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


}
