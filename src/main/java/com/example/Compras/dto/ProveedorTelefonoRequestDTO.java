package com.example.Compras.dto;

import jakarta.validation.constraints.NotNull;

public class ProveedorTelefonoRequestDTO {
    @NotNull(message = "El id del proveedor es obligatorio.")
    private Long idProveedor;

    @NotNull(message = "El id del teléfono es obligatorio.")
    private Long idTelefono;


    public Long getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Long idProveedor) {
        this.idProveedor = idProveedor;
    }

    public Long getIdTelefono() {
        return idTelefono;
    }

    public void setIdTelefono(Long idTelefono) {
        this.idTelefono = idTelefono;
    }
}
