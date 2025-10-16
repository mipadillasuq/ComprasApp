package com.example.Compras.Services;

import com.example.Compras.Repositories.CompraRepository;
import com.example.Compras.Entities.Compra;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class CompraService {

    private CompraRepository compraRepository;

    public CompraService(CompraRepository compraRepository) {
        this.compraRepository = compraRepository;}

    public List<Compra> getAll() { return compraRepository.findAll();}

    public Optional<Compra> getCompraById(Long id) { return  compraRepository.findById(id);}

    public Compra save (Compra compra) { return compraRepository.save(compra); }

    public void eliminar(Long id) {
        compraRepository.deleteById(id);
    }
 }
