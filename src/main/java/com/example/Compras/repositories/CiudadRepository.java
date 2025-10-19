package com.example.Compras.repositories;

import com.example.Compras.entities.Ciudad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CiudadRepository extends JpaRepository<Ciudad, Integer> {
    Optional<Ciudad> findByNombreIgnoreCase(String nombre);
}
