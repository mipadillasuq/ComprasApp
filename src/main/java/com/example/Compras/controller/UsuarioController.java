package com.example.Compras.controller;

import com.example.Compras.dto.UsuarioRequestDTO;
import com.example.Compras.dto.UsuarioResponseDTO;
import com.example.Compras.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("usuarios")
@Tag(name = "Usuario", description = "Operaciones CRUD para usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    @Operation(summary = "Crear un usuario")
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crearUsuario(@Valid @RequestBody UsuarioRequestDTO request) {
        return ResponseEntity.ok(usuarioService.crearUsuario(request));
    }
    @Operation(summary = "Listar todos los usuarios")
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }
    @Operation(summary = "Obtener un usuario por ID")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }
    @Operation(summary = "Actualizar un usuario")
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuario(@PathVariable Long id,
                                                                @Valid @RequestBody UsuarioRequestDTO request) {
        return ResponseEntity.ok(usuarioService.actualizarUsuario(id, request));
    }
    @Operation(summary = "Cambiar estado al usuario")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(@PathVariable Long id, @RequestParam Byte estado) {
        usuarioService.cambiarEstado(id, estado);
        return ResponseEntity.noContent().build();
    }
    @Operation(summary = "Eliminar un usuario")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
