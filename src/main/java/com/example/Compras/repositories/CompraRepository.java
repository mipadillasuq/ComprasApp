package com.example.Compras.repositories;

import com.example.Compras.entities.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {

    // ✅ Permite validar facturas duplicadas
    Optional<Compra> findByNumFactura(String numFactura);
}

