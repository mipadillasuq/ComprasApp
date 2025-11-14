package com.example.Compras.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class CompraResponseDTO {
    private Long id;
    private LocalDate fecha;
    private String numFactura;
    private Long idProveedor;
    private Long idUsuario;
}



