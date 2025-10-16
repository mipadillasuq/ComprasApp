package com.example.Compras.Services;

import com.example.Compras.Entities.DetalleCompra;
import com.example.Compras.Repositories.DetalleCompraRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DetalleCompraService {

    private DetalleCompraRepository detalleCompraRepository;

    public DetalleCompraService(DetalleCompraRepository detalleCompraRepository){
        this.detalleCompraRepository = detalleCompraRepository;
    }

    public List<DetalleCompra> getAll (){
        return detalleCompraRepository.findAll();
    }

    public Optional<DetalleCompra> getDetalleCompraById (Long id){
        return detalleCompraRepository.findById(id);
    }

    public DetalleCompra save (DetalleCompra detalleCompra) {
        return detalleCompraRepository.save(detalleCompra);
    }

    public void eliminar(Long id) {
        detalleCompraRepository.deleteById(id);
    }
}
