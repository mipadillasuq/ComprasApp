package com.example.Compras.entities;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="Compras")
public class Compra {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_compra")
    private Long id;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "num_factura", nullable = false, length = 50)
    private String numFactura;


    @ManyToOne
    @JoinColumn(name = "id_proveedor", nullable = false)
    private Proveedor proveedor;


    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;


    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleCompra> detalles;

    public Compra(Long id, LocalDate fecha, String numFactura, Proveedor proveedor, Usuario usuario, List<DetalleCompra> detalles) {
        this.id = id;
        this.fecha = fecha;
        this.numFactura = numFactura;
        this.proveedor = proveedor;
        this.usuario = usuario;
        this.detalles = detalles;
    }

    public Compra() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getNumFactura() { return numFactura; }
    public void setNumFactura(String numFactura) { this.numFactura = numFactura; }

    public Proveedor getProveedor() { return proveedor; }
    public void setProveedor(Proveedor proveedor) { this.proveedor = proveedor; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public List<DetalleCompra> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleCompra> detalles) { this.detalles = detalles; }
}