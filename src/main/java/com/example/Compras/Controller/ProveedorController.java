package com.example.Compras.Controller;

import com.example.Compras.Services.ProveedorService;
import com.example.Compras.Entities.Proveedor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("proveedores")
@Tag(name = "Proveedores", description = "Operaciones CRUD para proveedores")

public class ProveedorController {
    private ProveedorService proveedorService;

    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @Operation(summary = "Obtener un proveedor por ID")
    @GetMapping("/{id}")
    public Optional<Proveedor> getProveedor(@PathVariable Long id){return proveedorService.getProveedorById(id); }

    @Operation(summary = "Listar todos los proveedores")
    @GetMapping()
    public List<Proveedor> getAll() {return proveedorService.getAll(); }

    @Operation(summary = "Crear un proveedor")
    @PostMapping()
    public ResponseEntity<Proveedor> save(@RequestBody Proveedor proveedor) {
        Proveedor nuevo = proveedorService.save(proveedor);
        return ResponseEntity.ok(nuevo);
    }

    @Operation(summary = "Actualizar un proveedor")
    @PutMapping("/{id}")
    public ResponseEntity<Proveedor> actualizar(@PathVariable Long id, @RequestBody Proveedor proveedor) {
        return proveedorService.getProveedorById(id)
                .map(p -> {
                    proveedor.setIdProveedor(id);
                    return ResponseEntity.ok(proveedorService.save(proveedor));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar un proveedor")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (proveedorService.getProveedorById(id).isPresent()) {
            proveedorService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}


