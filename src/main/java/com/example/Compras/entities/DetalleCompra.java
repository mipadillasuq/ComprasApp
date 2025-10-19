package com.example.Compras.entities;

import jakarta.persistence.*;

@Entity
@Table(name="detalle_compra")
public class DetalleCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_compra")
    private Long idDetalleCompra;

    private Integer cantidad;

    @ManyToOne
    @JoinColumn(name = "id_compra", nullable = false)
    private Compra compra;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    public DetalleCompra(Long idDetalleCompra, Integer cantidad, Compra compra, Producto producto) {
        this.idDetalleCompra = idDetalleCompra;
        this.cantidad = cantidad;
        this.compra = compra;
        this.producto = producto;
    }

    public DetalleCompra() {
    }

    public Long getIdDetalleCompra() { return idDetalleCompra; }
    public void setIdDetalleCompra(Long idDetalleCompra) { this.idDetalleCompra = idDetalleCompra; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public Compra getCompra() { return compra; }
    public void setCompra(Compra compra) { this.compra = compra; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
}
