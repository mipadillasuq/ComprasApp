package com.example.Compras.dto;

import lombok.Data;

@Data
public class DetalleCompraResponseDTO {
    private Long id;
    private Long idProducto;
    private Integer cantidad;
    private Long idCompra;  // <-- AGREGAR
}


