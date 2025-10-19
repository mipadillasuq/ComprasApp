package com.example.Compras.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public class CompraRequestDTO {

    @NotNull
    private LocalDate fecha;

    @NotBlank
    private String numFactura;

    @NotNull
    private Long idProveedor;

    @NotNull
    private Long idUsuario;

    // ✅ Si al crear la compra también se envían los detalles
    private List<DetalleCompraRequestDTO> detalles;

    // Getters y Setters

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getNumFactura() { return numFactura; }
    public void setNumFactura(String numFactura) { this.numFactura = numFactura; }

    public Long getIdProveedor() { return idProveedor; }
    public void setIdProveedor(Long idProveedor) { this.idProveedor = idProveedor; }

    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public List<DetalleCompraRequestDTO> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleCompraRequestDTO> detalles) { this.detalles = detalles; }
}
