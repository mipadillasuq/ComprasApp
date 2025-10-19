package com.example.Compras.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "telefonos")
public class Telefono {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_telefono")
    private Long id;

    @Column(name = "numero", nullable = false, length = 15)
    private String numero;

    @OneToMany(mappedBy = "telefono", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<ProveedorTelefono> proveedoresTelefonos;

    public Telefono(Long id, String numero) {
        this.id = id;
        this.numero = numero;
    }

    public Telefono() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
}
