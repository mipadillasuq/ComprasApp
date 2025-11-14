package com.example.Compras.repositories;

import com.example.Compras.entities.Impuesto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImpuestoRepository extends JpaRepository<Impuesto, Long> {
    Optional<Impuesto> findByNombreIgnoreCase(String nombre);
}
