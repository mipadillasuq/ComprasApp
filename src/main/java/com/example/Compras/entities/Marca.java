package com.example.Compras.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "marca")
public class Marca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_marca")
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @OneToMany(mappedBy = "marca", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Producto> productos;

    public Marca() {
    }

    public Marca(Long id, String nombre, List<Producto> productos) {
        this.id = id;
        this.nombre = nombre;
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

}