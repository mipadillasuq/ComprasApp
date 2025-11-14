package com.example.Compras.repositories;

import com.example.Compras.entities.DetalleCompra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetalleCompraRepository extends JpaRepository<DetalleCompra, Long> {

    List<DetalleCompra> findByCompra_Id(Long idCompra);

}
