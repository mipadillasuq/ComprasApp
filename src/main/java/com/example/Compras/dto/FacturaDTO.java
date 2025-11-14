package com.example.Compras.dto;

import lombok.Data;

import java.util.List;

@Data
public class FacturaDTO {

    private Long idCompra;
    private String fecha;
    private String numFactura;

    private String proveedorNombre;
    private String proveedorEmail;
    private String proveedorDireccion;

    private String usuarioNombre;

    private List<ItemFactura> detalles;

    private Double total;

    @Data
    public static class ItemFactura {
        private String producto;
        private Integer cantidad;
        private Double precioUnitario;
        private Double subtotal;
    }
}

