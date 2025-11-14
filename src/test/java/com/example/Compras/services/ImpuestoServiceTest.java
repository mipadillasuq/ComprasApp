package com.example.Compras.services;

import com.example.Compras.dto.ImpuestoRequestDTO;
import com.example.Compras.dto.ImpuestoResponseDTO;
import com.example.Compras.entities.Impuesto;
import com.example.Compras.repositories.ImpuestoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ImpuestoServiceTest {

    @Mock
    private ImpuestoRepository impuestoRepository;

    @InjectMocks
    private ImpuestoService impuestoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // 🧩 Crear impuesto nuevo
    @Test
    void crearImpuesto_DeberiaCrearCorrectamente() {
        ImpuestoRequestDTO request = new ImpuestoRequestDTO();
        request.setNombre("IVA");
        request.setPorcentaje(19.0);

        when(impuestoRepository.findByNombreIgnoreCase("IVA")).thenReturn(Optional.empty());
        when(impuestoRepository.save(any(Impuesto.class)))
                .thenAnswer(invocation -> {
                    Impuesto imp = invocation.getArgument(0);
                    imp.setId(1L);
                    return imp;
                });

        ImpuestoResponseDTO result = impuestoService.crearImpuesto(request);

        assertNotNull(result);
        assertEquals("IVA", result.getNombre());
        assertEquals(19.0, result.getPorcentaje());
        verify(impuestoRepository).save(any(Impuesto.class));
    }

    @Test
    void crearImpuesto_DeberiaLanzarExcepcionSiExiste() {
        ImpuestoRequestDTO request = new ImpuestoRequestDTO();
        request.setNombre("IVA");
        request.setPorcentaje(19.0);

        when(impuestoRepository.findByNombreIgnoreCase("IVA"))
                .thenReturn(Optional.of(new Impuesto()));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> impuestoService.crearImpuesto(request));

        assertTrue(ex.getMessage().contains("Ya existe un impuesto"));
        verify(impuestoRepository, never()).save(any());
    }

    // 🧩 Actualizar impuesto existente
    @Test
    void actualizarImpuesto_DeberiaActualizarCorrectamente() {
        ImpuestoRequestDTO request = new ImpuestoRequestDTO();
        request.setNombre("IVA");
        request.setPorcentaje(21.0);

        Impuesto existente = new Impuesto();
        existente.setId(1L);
        existente.setNombre("Impuesto Anterior");
        existente.setPorcentaje(18.0);

        when(impuestoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(impuestoRepository.findByNombreIgnoreCase("IVA")).thenReturn(Optional.empty());
        when(impuestoRepository.save(any(Impuesto.class))).thenReturn(existente);

        ImpuestoResponseDTO result = impuestoService.actualizarImpuesto(1L, request);

        assertEquals("IVA", result.getNombre());
        assertEquals(21.0, result.getPorcentaje());
        verify(impuestoRepository).save(any(Impuesto.class));
    }

    @Test
    void actualizarImpuesto_DeberiaLanzarErrorSiNoExiste() {
        ImpuestoRequestDTO request = new ImpuestoRequestDTO();
        request.setNombre("Nuevo Impuesto");

        when(impuestoRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> impuestoService.actualizarImpuesto(99L, request));

        assertTrue(ex.getMessage().contains("No se encontró el impuesto"));
    }

    @Test
    void actualizarImpuesto_DeberiaLanzarErrorSiNombreDuplicado() {
        ImpuestoRequestDTO request = new ImpuestoRequestDTO();
        request.setNombre("IVA");

        Impuesto existente = new Impuesto();
        existente.setId(1L);

        Impuesto otro = new Impuesto();
        otro.setId(2L);
        otro.setNombre("IVA");

        when(impuestoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(impuestoRepository.findByNombreIgnoreCase("IVA")).thenReturn(Optional.of(otro));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> impuestoService.actualizarImpuesto(1L, request));

        assertTrue(ex.getMessage().contains("Ya existe otro impuesto"));
    }

    // 🧩 Eliminar impuesto
    @Test
    void eliminarImpuesto_DeberiaEliminarCorrectamente() {
        when(impuestoRepository.existsById(1L)).thenReturn(true);

        impuestoService.eliminarImpuesto(1L);

        verify(impuestoRepository).deleteById(1L);
    }

    @Test
    void eliminarImpuesto_DeberiaLanzarErrorSiNoExiste() {
        when(impuestoRepository.existsById(10L)).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> impuestoService.eliminarImpuesto(10L));

        assertTrue(ex.getMessage().contains("No se encontró el impuesto"));
    }

    // 🧩 Listar y obtener
    @Test
    void listarImpuestos_DeberiaRetornarListaDeDTOs() {
        Impuesto i1 = new Impuesto();
        i1.setId(1L);
        i1.setNombre("IVA");
        i1.setPorcentaje(19.0);

        Impuesto i2 = new Impuesto();
        i2.setId(2L);
        i2.setNombre("Retefuente");
        i2.setPorcentaje(2.0);

        when(impuestoRepository.findAll()).thenReturn(Arrays.asList(i1, i2));

        List<ImpuestoResponseDTO> result = impuestoService.listarImpuestos();

        assertEquals(2, result.size());
        assertEquals("Retefuente", result.get(1).getNombre());
        verify(impuestoRepository).findAll();
    }

    @Test
    void obtenerImpuestoPorId_DeberiaRetornarCorrectamente() {
        Impuesto i = new Impuesto();
        i.setId(1L);
        i.setNombre("IVA");
        i.setPorcentaje(19.0);

        when(impuestoRepository.findById(1L)).thenReturn(Optional.of(i));

        ImpuestoResponseDTO result = impuestoService.obtenerImpuestoPorId(1L);

        assertEquals("IVA", result.getNombre());
        assertEquals(19.0, result.getPorcentaje());
        verify(impuestoRepository).findById(1L);
    }

    @Test
    void obtenerImpuestoPorId_DeberiaLanzarErrorSiNoExiste() {
        when(impuestoRepository.findById(5L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> impuestoService.obtenerImpuestoPorId(5L));

        assertTrue(ex.getMessage().contains("No se encontró el impuesto"));
    }
}
