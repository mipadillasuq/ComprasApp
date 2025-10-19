package com.example.Compras.entities;

import jakarta.persistence.*;
import com.example.Compras.enums.Rol;

import java.util.List;

@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @Column(name = "nombre", length = 50)
    private String nombre;

    @Column(name = "email", unique = true, length = 100)
    private String email;

    @Column(name = "telefono", length = 15)
    private String telefono;

    @Column(name = "password", length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", length = 20)
    private Rol rol;

    @Column(name = "estado")
    private Byte estado = 1;

    @OneToMany(mappedBy = "usuario")
    private List<Compra> compras;


    public Usuario(Long id, String nombre, String email, String telefono, String password, Rol rol, Byte estado, List<Compra> compras) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.password = password;
        this.rol = rol;
        this.estado = estado;
        this.compras = compras;
    }

    public Usuario() {
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Long getId(){
        return id; }
    public void setId(Long id){
        this.id = id; }
    public String getNombre(){
        return nombre; }
    public void setNombre(String nombre){
        this.nombre = nombre; }
    public String getEmail(){
        return email; }
    public void setEmail(String email){
        this.email = email; }
    public String getTelefono(){
        return telefono; }
    public void setTelefono(String telefono){
        this.telefono = telefono; }
    public String getPassword(){
        return password; }
    public void setPassword(String password){
        this.password = password; }
    public Byte getEstado(){
        return estado; }
    public void setEstado(Byte estado){
        this.estado = estado; }
}
