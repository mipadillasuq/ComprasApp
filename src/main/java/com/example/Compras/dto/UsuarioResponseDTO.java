package com.example.Compras.dto;

public class UsuarioResponseDTO {

    private Long id;
    private String nombre;
    private String email;
    private String telefono;
    private Byte estado;
    private String rol; // ✅ Nuevo campo

    // ✅ Constructor completo (incluye rol)
    public UsuarioResponseDTO(Long id, String nombre, String email, String telefono, Byte estado, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.estado = estado;
        this.rol = rol;
    }

    // ✅ Constructor sin rol (por compatibilidad con métodos anteriores)
    public UsuarioResponseDTO(Long id, String nombre, String email, String telefono, Byte estado) {
        this(id, nombre, email, telefono, estado, null);
    }

    // ✅ Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public Byte getEstado() { return estado; }
    public void setEstado(Byte estado) { this.estado = estado; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}

