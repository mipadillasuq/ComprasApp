package com.example.Compras.controller;

import com.example.Compras.dto.CompraRequestDTO;
import com.example.Compras.dto.CompraResponseDTO;
import com.example.Compras.services.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/compras")
@CrossOrigin(origins = "http://localhost:4200") // ✅ permite acceso directo desde Angular
public class CompraController {

    private final CompraService compraService;

    @Autowired
    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }



    // ✅ Crear nueva compra
    @PostMapping
    public ResponseEntity<?> crearCompra(@RequestBody CompraRequestDTO request) {
        try {
            CompraResponseDTO response = compraService.crearCompra(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException ex) {
            // Muestra el error de negocio, por ejemplo "Proveedor no encontrado"
            ex.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("⚠️ Error al crear la compra: " + ex.getMessage());
        } catch (Exception e) {
            // Muestra cualquier otro error no controlado
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Error inesperado en el servidor: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCompra(@PathVariable Long id) {
        compraService.eliminarCompra(id);
        return ResponseEntity.noContent().build();
    }



    // ✅ (opcional) Obtener todas las compras para tu tabla
    @GetMapping
    public ResponseEntity<?> listarCompras() {
        try {
            return ResponseEntity.ok(compraService.getAllCompras());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Error al listar las compras: " + e.getMessage());
        }
    }
}



