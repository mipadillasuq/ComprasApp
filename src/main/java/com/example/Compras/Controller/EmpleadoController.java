package com.example.Compras.Controller;

import com.example.Compras.Entities.Empleado;
import com.example.Compras.Entities.Proveedor;
import com.example.Compras.Services.EmpleadoService;
import com.example.Compras.Services.ProveedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("empleados")
@Tag(name = "Empleados", description = "Operaciones CRUD para empleados")
public class EmpleadoController {
    private EmpleadoService empleadoService;

    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @Operation(summary = "Obtener un empleado por ID")
    @GetMapping("/{id}")
    public Optional<Empleado> getEmpleado(@PathVariable Long id){return empleadoService.getEmpleadoById(id); }

    @Operation(summary = "Listar todos los empleados")
    @GetMapping()
    public List<Empleado> getAll() {return empleadoService.getAll(); }

    @Operation(summary = "Crear un empleado")
    @PostMapping()
    public ResponseEntity<Empleado> save(@RequestBody Empleado empleado) {
        Empleado nuevo = empleadoService.save(empleado);
        return ResponseEntity.ok(nuevo);
    }

    @Operation(summary = "Actualizar un empleado")
    @PutMapping("/{id}")
    public ResponseEntity<Empleado> actualizar(@PathVariable Long id, @RequestBody Empleado empleado) {
        return empleadoService.getEmpleadoById(id)
                .map(p -> {
                    empleado.setIdEmpleado(id);
                    return ResponseEntity.ok(empleadoService.save(empleado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar un empleado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (empleadoService.getEmpleadoById(id).isPresent()) {
            empleadoService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
