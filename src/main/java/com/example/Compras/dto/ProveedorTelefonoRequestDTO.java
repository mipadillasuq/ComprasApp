package com.example.Compras.dto;

import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

public class ProveedorTelefonoRequestDTO {

    @NotNull(message = "El id del proveedor es obligatorio.")
    @Schema(example = "1", description = "ID del proveedor existente en la base de datos")
    private Long idProveedor;

    @NotNull(message = "El id del teléfono es obligatorio.")
    @Schema(example = "2", description = "ID del teléfono existente en la base de datos")
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
