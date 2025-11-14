package com.example.Compras.repositories;

import com.example.Compras.entities.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

    // 🔎 Buscar por nombre (ignorando mayúsculas/minúsculas)
    Optional<Proveedor> findByNombreIgnoreCase(String nombre);

    // 🔎 Buscar por email
    Optional<Proveedor> findByEmailIgnoreCase(String email);

    // 🧩 Verificar existencia por email (para validación previa)
    boolean existsByEmailIgnoreCase(String email);

    // 🧩 Consultar proveedores activos
    @Query("SELECT p FROM Proveedor p WHERE p.estado = true")
    java.util.List<Proveedor> findActivos();
}

