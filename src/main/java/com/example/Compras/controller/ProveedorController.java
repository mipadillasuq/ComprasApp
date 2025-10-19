package com.example.Compras.controller;

import com.example.Compras.dto.ProveedorRequestDTO;
import com.example.Compras.dto.ProveedorResponseDTO;
import com.example.Compras.dto.TelefonoRequestDTO;
import com.example.Compras.services.ProveedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("proveedores")
@Tag(name = "Proveedores", description = "Operaciones CRUD para proveedores")

public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    @Operation(summary = "Crear un nuevo proveedor")
    @PostMapping
    public ResponseEntity<ProveedorResponseDTO> crear(@Valid @RequestBody ProveedorRequestDTO dto) {
        return ResponseEntity.ok(proveedorService.crearProveedor(dto));
    }

    @Operation(summary = "Obtener un proveedor por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ProveedorResponseDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(proveedorService.getProveedor(id));
    }

    @Operation(summary = "Listar todos los proveedores")
    @GetMapping
    public ResponseEntity<List<ProveedorResponseDTO>> listar() {
        return ResponseEntity.ok(proveedorService.listarTodos());
    }

    @Operation(summary = "Eliminar un proveedor")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        proveedorService.eliminarProveedor(id);
        return ResponseEntity.ok("Proveedor eliminado");
    }

    @Operation(summary = "Agregar un teléfono a un proveedor")
    @PostMapping("/{id}/telefonos")
    public ResponseEntity<ProveedorResponseDTO> agregarTelefono(@PathVariable Long id, @Valid @RequestBody TelefonoRequestDTO dto) {
        return ResponseEntity.ok(proveedorService.agregarTelefono(id, dto));
    }

    @Operation(summary = "Eliminar un teléfono a un proveedor")
    @DeleteMapping("/{id}/telefonos/{idTelefono}")
    public ResponseEntity<ProveedorResponseDTO> quitarTelefono(@PathVariable Long id, @PathVariable Long idTelefono) {
        return ResponseEntity.ok(proveedorService.quitarTelefono(id, idTelefono));
    }
}


