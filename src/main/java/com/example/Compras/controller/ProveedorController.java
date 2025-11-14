package com.example.Compras.controller;

import com.example.Compras.dto.ProveedorRequestDTO;
import com.example.Compras.dto.ProveedorResponseDTO;
import com.example.Compras.dto.TelefonoRequestDTO;
import com.example.Compras.services.ProveedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/proveedores")
@Tag(name = "Proveedores", description = "Operaciones CRUD para proveedores")
public class ProveedorController {

    private final ProveedorService proveedorService;

    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    // =========================================================
    // 🟢 Crear proveedor
    // =========================================================
    @Operation(summary = "Crear un nuevo proveedor")
    @PostMapping
    public ResponseEntity<ProveedorResponseDTO> crear(@Valid @RequestBody ProveedorRequestDTO dto) {
        ProveedorResponseDTO creado = proveedorService.crearProveedor(dto);
        return ResponseEntity.ok(creado);
    }

    // =========================================================
    // 🟢 Obtener proveedor por ID
    // =========================================================
    @Operation(summary = "Obtener un proveedor por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ProveedorResponseDTO> obtener(@PathVariable Long id) {
        ProveedorResponseDTO proveedor = proveedorService.getProveedor(id);
        return ResponseEntity.ok(proveedor);
    }

    // =========================================================
    // 🟢 Listar todos los proveedores
    // =========================================================
    @Operation(summary = "Listar todos los proveedores")
    @GetMapping
    public ResponseEntity<List<ProveedorResponseDTO>> listar() {
        List<ProveedorResponseDTO> lista = proveedorService.listarTodos();
        return ResponseEntity.ok(lista);
    }

    // =========================================================
    // 🟡 Actualizar proveedor
    // =========================================================
    @Operation(summary = "Actualizar un proveedor existente")
    @PutMapping("/{id}")
    public ResponseEntity<ProveedorResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProveedorRequestDTO dto) {
        ProveedorResponseDTO actualizado = proveedorService.actualizarProveedor(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    // =========================================================
    // 🔴 Eliminar proveedor
    // =========================================================
    @Operation(summary = "Eliminar un proveedor")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        proveedorService.eliminarProveedor(id);
        return ResponseEntity.noContent().build(); // ✅ 204 sin cuerpo
    }

    // =========================================================
    // 📞 Agregar teléfono a proveedor
    // =========================================================
    @Operation(summary = "Agregar un teléfono a un proveedor")
    @PostMapping("/{id}/telefonos")
    public ResponseEntity<ProveedorResponseDTO> agregarTelefono(
            @PathVariable Long id,
            @Valid @RequestBody TelefonoRequestDTO dto) {
        ProveedorResponseDTO actualizado = proveedorService.agregarTelefono(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    // =========================================================
    // 🧹 Quitar teléfono de proveedor
    // =========================================================
    @Operation(summary = "Eliminar un teléfono de un proveedor")
    @DeleteMapping("/{id}/telefonos/{idTelefono}")
    public ResponseEntity<ProveedorResponseDTO> quitarTelefono(
            @PathVariable Long id,
            @PathVariable Long idTelefono) {
        ProveedorResponseDTO actualizado = proveedorService.quitarTelefono(id, idTelefono);
        return ResponseEntity.ok(actualizado);
    }
}



