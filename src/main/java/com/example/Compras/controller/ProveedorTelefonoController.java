package com.example.Compras.controller;
import com.example.Compras.dto.ProveedorTelefonoRequestDTO;
import com.example.Compras.dto.ProveedorTelefonoResponseDTO;
import com.example.Compras.services.ProveedorTelefonoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("proveedor_telefono")
@Tag(name = "Proveedor-Teléfono", description = "Operaciones CRUD para proveedores-teléfonos")
public class ProveedorTelefonoController {

    private final ProveedorTelefonoService proveedorTelefonoService;

    public ProveedorTelefonoController(ProveedorTelefonoService proveedorTelefonoService) {
        this.proveedorTelefonoService = proveedorTelefonoService;
    }

    @Operation(summary = "Crear relación telefonos y proveedores")
    @PostMapping
    public ResponseEntity<ProveedorTelefonoResponseDTO> crearRelacion(
            @Valid @RequestBody ProveedorTelefonoRequestDTO request) {
        return ResponseEntity.ok(proveedorTelefonoService.crearRelacion(request));
    }

    @Operation(summary = "Listar todas las relaciones proveedor-teléfono")
    @GetMapping
    public ResponseEntity<List<ProveedorTelefonoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(proveedorTelefonoService.listarTodos());
    }

    @Operation(summary = "Listar los telefonos por proveedor")
    @GetMapping("/proveedor/{idProveedor}")
    public ResponseEntity<List<ProveedorTelefonoResponseDTO>> listarPorProveedor(@PathVariable Long idProveedor) {
        return ResponseEntity.ok(proveedorTelefonoService.listarPorProveedor(idProveedor));
    }

    @Operation(summary = "Eliminar relacion telefonos y proveedores")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRelacion(@PathVariable Long id) {
        proveedorTelefonoService.eliminarRelacion(id);
        return ResponseEntity.noContent().build();
    }
}
