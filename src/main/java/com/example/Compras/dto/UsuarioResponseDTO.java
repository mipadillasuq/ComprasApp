package com.example.Compras.dto;

public class UsuarioResponseDTO {
    private Long id;
    private String nombre;
    private String email;
    private String telefono;
    private Byte estado;

    public UsuarioResponseDTO(Long id, String nombre, String email, String telefono, Byte estado) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.estado = estado;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getTelefono() { return telefono; }
    public Byte getEstado() { return estado; }

}
