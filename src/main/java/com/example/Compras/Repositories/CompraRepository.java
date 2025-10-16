package com.example.Compras.Repositories;

import com.example.Compras.Entities.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface CompraRepository extends JpaRepository<Compra, Long> {
}
