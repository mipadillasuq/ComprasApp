package com.example.Compras.dto;

import java.time.LocalDate;
import java.util.List;

public class CompraResponseDTO {

    private Long id;
    private LocalDate fecha;
    private String numFactura;
    private Long idProveedor;
    private Long idUsuario;

    // ✅ Para devolver los detalles si se requieren
    private List<DetalleCompraResponseDTO> detalles;

    // Getters y Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getNumFactura() { return numFactura; }
    public void setNumFactura(String numFactura) { this.numFactura = numFactura; }

    public Long getIdProveedor() { return idProveedor; }
    public void setIdProveedor(Long idProveedor) { this.idProveedor = idProveedor; }

    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public List<DetalleCompraResponseDTO> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleCompraResponseDTO> detalles) { this.detalles = detalles; }
}
