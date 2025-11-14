package com.example.Compras.services;
import com.example.Compras.dto.FacturaDTO;
import com.example.Compras.entities.Compra;
import com.example.Compras.entities.DetalleCompra;
import com.example.Compras.entities.Producto;
import com.example.Compras.repositories.CompraRepository;
import com.example.Compras.repositories.DetalleCompraRepository;
import com.example.Compras.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FacturaService {

    private final CompraRepository compraRepository;
    private final DetalleCompraRepository detalleCompraRepository;

    public FacturaService(CompraRepository compraRepository,
                          DetalleCompraRepository detalleCompraRepository) {
        this.compraRepository = compraRepository;
        this.detalleCompraRepository = detalleCompraRepository;
    }

    public FacturaDTO obtenerFactura(Long idCompra) {

        Compra compra = compraRepository.findById(idCompra)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));

        FacturaDTO factura = new FacturaDTO();
        factura.setIdCompra(compra.getId());
        factura.setFecha(compra.getFecha().toString());
        factura.setNumFactura(compra.getNumFactura());

        // -----------------------
        //    Datos proveedor
        // -----------------------
        factura.setProveedorNombre(compra.getProveedor().getNombre());
        factura.setProveedorEmail(compra.getProveedor().getEmail());
        factura.setProveedorDireccion(compra.getProveedor().getDireccion());

        // -----------------------
        //    Datos usuario
        // -----------------------
        factura.setUsuarioNombre(compra.getUsuario().getNombre());

        // -----------------------
        //    Detalles + total
        // -----------------------
        List<FacturaDTO.ItemFactura> items = new ArrayList<>();
        double total = 0.0;

        List<DetalleCompra> detalles = detalleCompraRepository.findByCompra_Id(idCompra);

        for (DetalleCompra d : detalles) {

            Producto p = d.getProducto();

            FacturaDTO.ItemFactura item = new FacturaDTO.ItemFactura();
            item.setProducto(p.getNombre());
            item.setCantidad(d.getCantidad());
            item.setPrecioUnitario(p.getPrecio());

            double subtotal = p.getPrecio() * d.getCantidad();
            item.setSubtotal(subtotal);

            total += subtotal;

            items.add(item);
        }

        factura.setDetalles(items);
        factura.setTotal(total);

        return factura;
    }
}


