package com.example.Compras.services;

import com.example.Compras.dto.ImpuestoRequestDTO;
import com.example.Compras.dto.ImpuestoResponseDTO;
import com.example.Compras.entities.Impuesto;
import com.example.Compras.repositories.ImpuestoRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ImpuestoService {

    private final ImpuestoRepository impuestoRepository;

    public ImpuestoService(ImpuestoRepository impuestoRepository) {
        this.impuestoRepository = impuestoRepository;
    }

    // Crear un nuevo impuesto
    public ImpuestoResponseDTO crearImpuesto(ImpuestoRequestDTO request) {
        // Validar duplicado por nombre
        impuestoRepository.findByNombreIgnoreCase(request.getNombre())
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Ya existe un impuesto con el nombre '" + request.getNombre() + "'");
                });

        Impuesto impuesto = new Impuesto();
        impuesto.setNombre(request.getNombre());
        impuesto.setPorcentaje(request.getPorcentaje());

        impuestoRepository.save(impuesto);
        return new ImpuestoResponseDTO(impuesto.getId(), impuesto.getNombre(), impuesto.getPorcentaje());
    }

    // Actualizar impuesto existente
    public ImpuestoResponseDTO actualizarImpuesto(Integer id, ImpuestoRequestDTO request) {
        Impuesto impuesto = impuestoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el impuesto con id " + id));

        // Validar si el nombre nuevo ya existe
        impuestoRepository.findByNombreIgnoreCase(request.getNombre())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Ya existe otro impuesto con el nombre '" + request.getNombre() + "'");
                });

        impuesto.setNombre(request.getNombre());
        impuesto.setPorcentaje(request.getPorcentaje());
        impuestoRepository.save(impuesto);

        return new ImpuestoResponseDTO(impuesto.getId(), impuesto.getNombre(), impuesto.getPorcentaje());
    }

    // Eliminar impuesto
    public void eliminarImpuesto(Integer id) {
        if (!impuestoRepository.existsById(id)) {
            throw new IllegalArgumentException("No se encontró el impuesto con id " + id);
        }
        impuestoRepository.deleteById(id);
    }

    // Listar todos los impuestos
    @Transactional(readOnly = true)
    public List<ImpuestoResponseDTO> listarImpuestos() {
        return impuestoRepository.findAll().stream()
                .map(i -> new ImpuestoResponseDTO(i.getId(), i.getNombre(), i.getPorcentaje()))
                .collect(Collectors.toList());
    }

    // Obtener impuesto por ID
    @Transactional(readOnly = true)
    public ImpuestoResponseDTO obtenerImpuestoPorId(Integer id) {
        Impuesto impuesto = impuestoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el impuesto con id " + id));

        return new ImpuestoResponseDTO(impuesto.getId(), impuesto.getNombre(), impuesto.getPorcentaje());
    }
}