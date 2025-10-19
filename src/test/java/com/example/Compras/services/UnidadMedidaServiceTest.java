package com.example.Compras.services;

import com.example.Compras.dto.UnidadMedidaRequestDTO;
import com.example.Compras.dto.UnidadMedidaResponseDTO;
import com.example.Compras.entities.UnidadMedida;
import com.example.Compras.repositories.UnidadMedidaRepository;
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

class UnidadMedidaServiceTest {

    @Mock
    private UnidadMedidaRepository unidadMedidaRepository;

    @InjectMocks
    private UnidadMedidaService unidadMedidaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // 🧩 Crear unidad
    @Test
    void crearUnidad_DeberiaCrearCorrectamente() {
        UnidadMedidaRequestDTO request = new UnidadMedidaRequestDTO();
        request.setNombre("Kilogramo");

        when(unidadMedidaRepository.findByNombreIgnoreCase("Kilogramo"))
                .thenReturn(Optional.empty());
        when(unidadMedidaRepository.save(any(UnidadMedida.class)))
                .thenAnswer(inv -> {
                    UnidadMedida u = inv.getArgument(0);
                    u.setId(1L);
                    return u;
                });

        UnidadMedidaResponseDTO result = unidadMedidaService.crearUnidad(request);

        assertNotNull(result);
        assertEquals("Kilogramo", result.getNombre());
        verify(unidadMedidaRepository).save(any(UnidadMedida.class));
    }

    @Test
    void crearUnidad_DeberiaLanzarErrorSiYaExiste() {
        UnidadMedidaRequestDTO request = new UnidadMedidaRequestDTO();
        request.setNombre("Litro");

        when(unidadMedidaRepository.findByNombreIgnoreCase("Litro"))
                .thenReturn(Optional.of(new UnidadMedida()));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> unidadMedidaService.crearUnidad(request));

        assertTrue(ex.getMessage().contains("Ya existe"));
        verify(unidadMedidaRepository, never()).save(any());
    }

    // 🧩 Listar todas
    @Test
    void listarTodas_DeberiaRetornarLista() {
        UnidadMedida u1 = new UnidadMedida(); u1.setId(1L); u1.setNombre("Kg");
        UnidadMedida u2 = new UnidadMedida(); u2.setId(2L); u2.setNombre("Lt");

        when(unidadMedidaRepository.findAll()).thenReturn(Arrays.asList(u1, u2));

        List<UnidadMedidaResponseDTO> result = unidadMedidaService.listarTodas();

        assertEquals(2, result.size());
        assertEquals("Lt", result.get(1).getNombre());
        verify(unidadMedidaRepository).findAll();
    }

    // 🧩 Buscar por ID
    @Test
    void buscarPorId_DeberiaRetornarUnidadSiExiste() {
        UnidadMedida unidad = new UnidadMedida();
        unidad.setId(1L);
        unidad.setNombre("Metro");

        when(unidadMedidaRepository.findById(1))
                .thenReturn(Optional.of(unidad));

        UnidadMedidaResponseDTO result = unidadMedidaService.buscarPorId(1);

        assertEquals("Metro", result.getNombre());
        verify(unidadMedidaRepository).findById(1);
    }

    @Test
    void buscarPorId_DeberiaLanzarErrorSiNoExiste() {
        when(unidadMedidaRepository.findById(5)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> unidadMedidaService.buscarPorId(5));

        assertTrue(ex.getMessage().contains("No se encontró"));
    }

    // 🧩 Actualizar unidad
    @Test
    void actualizarUnidad_DeberiaActualizarCorrectamente() {
        UnidadMedidaRequestDTO request = new UnidadMedidaRequestDTO();
        request.setNombre("Centímetro");

        UnidadMedida existente = new UnidadMedida();
        existente.setId(1L);
        existente.setNombre("Metro");

        when(unidadMedidaRepository.findById(1)).thenReturn(Optional.of(existente));
        when(unidadMedidaRepository.findByNombreIgnoreCase("Centímetro")).thenReturn(Optional.empty());
        when(unidadMedidaRepository.save(any(UnidadMedida.class))).thenReturn(existente);

        UnidadMedidaResponseDTO result = unidadMedidaService.actualizarUnidad(1, request);

        assertEquals("Centímetro", result.getNombre());
        verify(unidadMedidaRepository).save(any(UnidadMedida.class));
    }

    @Test
    void actualizarUnidad_DeberiaLanzarErrorSiNoExiste() {
        UnidadMedidaRequestDTO request = new UnidadMedidaRequestDTO();
        request.setNombre("Mililitro");

        when(unidadMedidaRepository.findById(1)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> unidadMedidaService.actualizarUnidad(1, request));

        assertTrue(ex.getMessage().contains("No se encontró"));
    }

    @Test
    void actualizarUnidad_DeberiaLanzarErrorSiDuplicado() {
        UnidadMedidaRequestDTO request = new UnidadMedidaRequestDTO();
        request.setNombre("Litro");

        UnidadMedida existente = new UnidadMedida();
        existente.setId(1L);

        UnidadMedida duplicada = new UnidadMedida();
        duplicada.setId(2L);
        duplicada.setNombre("Litro");

        when(unidadMedidaRepository.findById(1)).thenReturn(Optional.of(existente));
        when(unidadMedidaRepository.findByNombreIgnoreCase("Litro")).thenReturn(Optional.of(duplicada));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> unidadMedidaService.actualizarUnidad(1, request));

        assertTrue(ex.getMessage().contains("Ya existe otra unidad"));
    }

    // 🧩 Eliminar unidad
    @Test
    void eliminarUnidad_DeberiaEliminarCorrectamente() {
        when(unidadMedidaRepository.existsById(1)).thenReturn(true);

        unidadMedidaService.eliminarUnidad(1);

        verify(unidadMedidaRepository).deleteById(1);
    }

    @Test
    void eliminarUnidad_DeberiaLanzarErrorSiNoExiste() {
        when(unidadMedidaRepository.existsById(10)).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> unidadMedidaService.eliminarUnidad(10));

        assertTrue(ex.getMessage().contains("No existe una unidad"));
    }
}
