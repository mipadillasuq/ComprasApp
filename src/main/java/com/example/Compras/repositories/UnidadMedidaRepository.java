package com.example.Compras.repositories;

import com.example.Compras.entities.UnidadMedida;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UnidadMedidaRepository extends JpaRepository<UnidadMedida, Integer> {
    Optional<UnidadMedida> findByNombreIgnoreCase(String nombre);
}
