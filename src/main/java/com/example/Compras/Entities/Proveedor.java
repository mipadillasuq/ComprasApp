package com.example.Compras.Entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="proveedores")

public class Proveedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long idProveedor;
    private String nombre;
    private String telefono;
    private String email;

    @OneToMany(mappedBy = "proveedor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Compra> compras;

    public Proveedor(Long idProveedor, String nombre, String telefono, String email) {
        this.idProveedor = idProveedor;
        this.email = email;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public Proveedor() {
    }

    public Long getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Long idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
