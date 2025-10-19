package com.example.Compras.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "unidades_de_medida")
public class UnidadMedida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_unidad_medida")
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @OneToMany(mappedBy = "unidadMedida")
    private List<Producto> productos;

    public UnidadMedida(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public UnidadMedida() {
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
