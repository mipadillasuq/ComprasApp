package com.example.Compras.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "precio")
    private Double precio;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "estado")
    private Boolean estado;

    @ManyToOne
    @JoinColumn(name = "categoria")
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "marca")
    private Marca marca;

    @ManyToOne
    @JoinColumn(name = "unidad_medida")
    private UnidadMedida unidadMedida;

    @Column(name = "cantidad_unidades_medida")
    private Integer cantidadUnidadesMedida;

    @ManyToOne
    @JoinColumn(name = "impuesto")
    private Impuesto impuesto;

    public Producto(Long id, String nombre, Categoria categoria, Marca marca, UnidadMedida unidadMedida, Integer cantidadUnidadesMedida, Impuesto impuesto, Double precio, Integer stock, Boolean estado) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.marca = marca;
        this.unidadMedida = unidadMedida;
        this.cantidadUnidadesMedida = cantidadUnidadesMedida;
        this.impuesto = impuesto;
        this.precio = precio;
        this.stock = stock;
        this.estado = estado;
    }

    public Producto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public UnidadMedida getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(UnidadMedida unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public Integer getCantidadUnidadesMedida() {
        return cantidadUnidadesMedida;
    }

    public void setCantidadUnidadesMedida(Integer cantidadUnidadesMedida) {
        this.cantidadUnidadesMedida = cantidadUnidadesMedida;
    }

    public Impuesto getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(Impuesto impuesto) {
        this.impuesto = impuesto;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}