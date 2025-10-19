package com.example.Compras.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProveedorRequestDTO {
    @NotNull
    private Long idProveedor;

    @NotBlank
    private String nombre;

    private Long ciudadId;
    private String direccion;

    @Email
    private String email;

    private Boolean estado;

    // getters / setters
    public Long getIdProveedor() { return idProveedor; }
    public void setIdProveedor(Long idProveedor) { this.idProveedor = idProveedor; }

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
}
