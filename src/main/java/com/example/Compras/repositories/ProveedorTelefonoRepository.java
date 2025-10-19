package com.example.Compras.repositories;


import com.example.Compras.entities.ProveedorTelefono;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProveedorTelefonoRepository extends JpaRepository<ProveedorTelefono, Long> {
    List<ProveedorTelefono> findByProveedorId(Long proveedorId);
    boolean existsByProveedorIdAndTelefonoId(Long proveedorId, Long telefonoId);
}
