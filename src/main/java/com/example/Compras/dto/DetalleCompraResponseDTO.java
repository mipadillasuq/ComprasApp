package com.example.Compras.dto;

public class DetalleCompraResponseDTO {
    private Long id;
    private Long idProducto;
    private Integer cantidad;

    // Getters y Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getIdProducto() { return idProducto; }
    public void setIdProducto(Long idProducto) { this.idProducto = idProducto; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
}
