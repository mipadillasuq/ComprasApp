package com.example.Compras.Services;

import com.example.Compras.Repositories.ProveedorRepository;
import com.example.Compras.Entities.Proveedor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProveedorService {
    private ProveedorRepository proveedorRepository;

    public ProveedorService (ProveedorRepository proveedorRepository){
        this.proveedorRepository = proveedorRepository;
    }

    public List <Proveedor> getAll (){
        return proveedorRepository.findAll();
    }

    public Optional<Proveedor> getProveedorById (Long id){
        return proveedorRepository.findById(id);
    }

    public Proveedor save (Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    public void eliminar(Long id) {
        proveedorRepository.deleteById(id);
    }
}
