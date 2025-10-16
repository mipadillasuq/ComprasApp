package com.example.Compras.Repositories;

import com.example.Compras.Entities.DetalleCompra;
import com.example.Compras.Entities.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

}
