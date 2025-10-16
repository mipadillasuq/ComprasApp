package com.example.Compras.Services;

import com.example.Compras.Entities.Empleado;
import com.example.Compras.Entities.Proveedor;
import com.example.Compras.Repositories.EmpleadoRepository;
import com.example.Compras.Repositories.ProveedorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoService {
    private EmpleadoRepository empleadoRepository;

    public EmpleadoService (EmpleadoRepository empleadoRepository){
        this.empleadoRepository = empleadoRepository;
    }

    public List<Empleado> getAll (){
        return empleadoRepository.findAll();
    }

    public Optional<Empleado> getEmpleadoById (Long id){
        return empleadoRepository.findById(id);
    }

    public Empleado save (Empleado empleado) {
        return empleadoRepository.save(empleado);
    }

    public void eliminar(Long id) {
        empleadoRepository.deleteById(id);
    }

}
