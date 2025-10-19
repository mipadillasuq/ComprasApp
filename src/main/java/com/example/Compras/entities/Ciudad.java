package com.example.Compras.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "ciudades")
public class Ciudad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ciudad")
    private Integer id;

    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;

    public Ciudad() {

    }

    public Ciudad(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Ciudad(String nombre) {
        this.nombre = nombre;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}