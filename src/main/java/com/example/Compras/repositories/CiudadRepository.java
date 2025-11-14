package com.example.Compras.repositories;

import com.example.Compras.entities.Ciudad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CiudadRepository extends JpaRepository<Ciudad, Long> {

    // 🔍 Buscar ciudad por nombre (ignorando mayúsculas/minúsculas)
    Optional<Ciudad> findByNombreIgnoreCase(String nombre);
}
