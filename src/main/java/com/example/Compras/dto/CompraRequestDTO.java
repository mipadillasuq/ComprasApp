package com.example.Compras.dto;

import lombok.Data;

@Data
public class CompraRequestDTO {
    private String fecha;
    private String numFactura;
    private Long idProveedor;
    private Long idUsuario;
}






