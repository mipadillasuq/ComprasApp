package com.example.Compras.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "impuestos")
public class Impuesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_impuesto")
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    // Se almacena como 0.19, 0.05, etc.
    @Column(name = "porcentaje", nullable = false)
    private Double porcentaje;

    @OneToMany(mappedBy = "impuesto", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Producto> productos;

    public Impuesto() {
    }

    public Impuesto(Long id, String nombre, Double porcentaje, List<Producto> productos) {
        this.id = id;
        this.nombre = nombre;
        this.porcentaje = porcentaje;
        this.productos = productos;
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

    public Double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(Double porcentaje) {
        this.porcentaje = porcentaje;
    }
}
