package com.example.Compras.controller;

import com.example.Compras.dto.DetalleCompraRequestDTO;
import com.example.Compras.dto.DetalleCompraResponseDTO;
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

    // ======================================================================
    //  🔵 LISTAR TODOS LOS DETALLES COMO DTO
    // ======================================================================
    @Operation(summary = "Listar todos los detalles de compra")
    @GetMapping
    public List<DetalleCompraResponseDTO> listar() {
        return detalleCompraService.getAllDto();
    }

    // ======================================================================
    //  🔵 OBTENER 1 DETALLE COMO DTO
    // ======================================================================
    @Operation(summary = "Obtener un detalle de compra por ID")
    @GetMapping("/{id}")
    public ResponseEntity<DetalleCompraResponseDTO> obtener(@PathVariable Long id) {
        return detalleCompraService.getByIdDto(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ======================================================================
    //  🔵 CREAR UN DETALLE DESDE DTO
    // ======================================================================
    @Operation(summary = "Crear un detalle de compra")
    @PostMapping
    public ResponseEntity<Void> crear(@RequestBody DetalleCompraRequestDTO dto) {
        try {
            detalleCompraService.crearDesdeDto(dto);
            return ResponseEntity.ok().build(); // ✅ NO DEVUELVE JSON → EVITA ERROR 500
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }


    // ======================================================================
    //  🔵 ELIMINAR UN DETALLE
    // ======================================================================
    @Operation(summary = "Eliminar un detalle de compra")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (detalleCompraService.getByIdDto(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        detalleCompraService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

