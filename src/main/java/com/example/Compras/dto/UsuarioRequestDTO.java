package com.example.Compras.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsuarioRequestDTO {
    @NotBlank(message = "El nombre es obligatorio.")
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres.")
    private String nombre;

    @NotBlank(message = "El correo electrónico es obligatorio.")
    @Email(message = "Debe ingresar un correo válido.")
    private String email;

    @NotBlank(message = "El teléfono es obligatorio.")
    @Size(max = 15, message = "El teléfono no puede tener más de 15 caracteres.")
    private String telefono;

    @NotBlank(message = "La contraseña es obligatoria.")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres.")
    private String password;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
