package com.example.Compras.repositories;

import com.example.Compras.entities.Marca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MarcaRepository extends JpaRepository<Marca, Long> {
    Optional<Marca> findByNombreIgnoreCase(String nombre);
}
