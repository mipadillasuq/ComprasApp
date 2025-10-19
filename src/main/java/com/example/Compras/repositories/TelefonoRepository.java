package com.example.Compras.repositories;

import com.example.Compras.entities.Telefono;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TelefonoRepository extends JpaRepository<Telefono, Long> {
    Optional<Telefono> findByNumero(String numero);
}
