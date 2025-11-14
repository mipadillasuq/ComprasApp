package com.example.Compras.controller;

import com.example.Compras.dto.FacturaDTO;
import com.example.Compras.services.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/factura")
public class FacturaController {

    private final FacturaService facturaService;

    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    @GetMapping("/{idCompra}")
    public ResponseEntity<FacturaDTO> getFactura(@PathVariable Long idCompra) {
        return ResponseEntity.ok(facturaService.obtenerFactura(idCompra));
    }
}


