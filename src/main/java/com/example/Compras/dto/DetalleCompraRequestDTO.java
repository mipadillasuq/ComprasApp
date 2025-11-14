package com.example.Compras.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class DetalleCompraRequestDTO {

    @NotNull
    private Long idCompra;      // ✅ NUEVO

    @NotNull
    private Long idProducto;

    @NotNull
    @Min(1)
    private Integer cantidad;

    // Getters y Setters

    public Long getIdCompra() { return idCompra; }
    public void setIdCompra(Long idCompra) { this.idCompra = idCompra; }

    public Long getIdProducto() { return idProducto; }
    public void setIdProducto(Long idProducto) { this.idProducto = idProducto; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
}



