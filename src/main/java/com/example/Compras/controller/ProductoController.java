package com.example.Compras.controller;

import com.example.Compras.entities.Producto;
import com.example.Compras.services.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("productos")
@Tag(name = "Productos", description = "Operaciones CRUD para productos")
public class ProductoController {
    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @Operation(summary = "Listar todos los productos")
    @GetMapping
    public List<Producto> listar() {
        return productoService.getAll();
    }
    @Operation(summary = "Obtener un producto por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        return productoService.getProductoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @Operation(summary = "Crear un producto")
    @PostMapping
    public Producto crear(@RequestBody Producto producto) {
        return productoService.guardar(producto);
    }

    @Operation(summary = "Actualizar un producto")
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id, @RequestBody Producto producto) {
        return productoService.getProductoById(id)
                .map(p -> {
                    producto.setId(id);
                    return ResponseEntity.ok(productoService.guardar(producto));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar un producto")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (productoService.getProductoById(id).isPresent()) {
            productoService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
