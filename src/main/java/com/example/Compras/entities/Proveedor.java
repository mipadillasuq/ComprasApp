package com.example.Compras.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "proveedores")
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proveedor", length = 20)
    private Long id; // varchar(20) según tu modelo

    @Column(name = "nombre", nullable = false)
    private String nombre;

    // si quieres mapear la ciudad por FK (en imagen es int)
    @Column(name = "ciudad")
    private Long ciudadId;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "email")
    private String email;

    @Column(name = "estado")
    private Boolean estado = true;


    @OneToMany(mappedBy = "proveedor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProveedorTelefono> telefonos;


    public Proveedor(Long id, String nombre, Long ciudadId, String direccion, String email, Boolean estado, List<ProveedorTelefono> telefonos) {
        this.id = id;
        this.nombre = nombre;
        this.ciudadId = ciudadId;
        this.direccion = direccion;
        this.email = email;
        this.estado = estado;
        this.telefonos = telefonos;
    }

    public Proveedor() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Long getCiudadId() { return ciudadId; }
    public void setCiudadId(Long ciudadId) { this.ciudadId = ciudadId; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }

    public List<ProveedorTelefono> getTelefonos() { return telefonos; }
    public void setTelefonos(List<ProveedorTelefono> telefonos) { this.telefonos = telefonos; }
}