package com.example.Compras.services;

import com.example.Compras.dto.MarcaRequestDTO;
import com.example.Compras.dto.MarcaResponseDTO;
import com.example.Compras.entities.Marca;
import com.example.Compras.repositories.MarcaRepository;
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

class MarcaServiceTest {

    @Mock
    private MarcaRepository marcaRepository;

    @InjectMocks
    private MarcaService marcaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // 🧩 Crear marca
    @Test
    void crearMarca_DeberiaCrearCorrectamente() {
        MarcaRequestDTO request = new MarcaRequestDTO();
        request.setNombre("Coca-Cola");

        when(marcaRepository.findByNombreIgnoreCase("Coca-Cola")).thenReturn(Optional.empty());
        when(marcaRepository.save(any(Marca.class))).thenAnswer(invocation -> {
            Marca m = invocation.getArgument(0);
            m.setId(1L);
            return m;
        });

        MarcaResponseDTO result = marcaService.crearMarca(request);

        assertNotNull(result);
        assertEquals("Coca-Cola", result.getNombre());
        verify(marcaRepository).save(any(Marca.class));
    }

    @Test
    void crearMarca_DeberiaLanzarErrorSiYaExiste() {
        MarcaRequestDTO request = new MarcaRequestDTO();
        request.setNombre("Pepsi");

        when(marcaRepository.findByNombreIgnoreCase("Pepsi"))
                .thenReturn(Optional.of(new Marca()));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> marcaService.crearMarca(request));

        assertTrue(ex.getMessage().contains("Ya existe una marca"));
        verify(marcaRepository, never()).save(any());
    }

    // 🧩 Actualizar marca
    @Test
    void actualizarMarca_DeberiaActualizarCorrectamente() {
        MarcaRequestDTO request = new MarcaRequestDTO();
        request.setNombre("Pepsi");

        Marca existente = new Marca();
        existente.setId(1L);
        existente.setNombre("Coca-Cola");

        when(marcaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(marcaRepository.findByNombreIgnoreCase("Pepsi")).thenReturn(Optional.empty());
        when(marcaRepository.save(any(Marca.class))).thenReturn(existente);

        MarcaResponseDTO result = marcaService.actualizarMarca(1L, request);

        assertEquals("Pepsi", result.getNombre());
        verify(marcaRepository).save(any(Marca.class));
    }

    @Test
    void actualizarMarca_DeberiaLanzarErrorSiNoExiste() {
        MarcaRequestDTO request = new MarcaRequestDTO();
        request.setNombre("Pepsi");

        when(marcaRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> marcaService.actualizarMarca(99L, request));

        assertTrue(ex.getMessage().contains("No se encontró la marca"));
    }

    @Test
    void actualizarMarca_DeberiaLanzarErrorSiNombreDuplicado() {
        MarcaRequestDTO request = new MarcaRequestDTO();
        request.setNombre("Pepsi");

        Marca existente = new Marca();
        existente.setId(1L);

        Marca otra = new Marca();
        otra.setId(2L);
        otra.setNombre("Pepsi");

        when(marcaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(marcaRepository.findByNombreIgnoreCase("Pepsi")).thenReturn(Optional.of(otra));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> marcaService.actualizarMarca(1L, request));

        assertTrue(ex.getMessage().contains("Ya existe otra marca"));
    }

    // 🧩 Eliminar marca
    @Test
    void eliminarMarca_DeberiaEliminarCorrectamente() {
        when(marcaRepository.existsById(1L)).thenReturn(true);

        marcaService.eliminarMarca(1L);

        verify(marcaRepository).deleteById(1L);
    }

    @Test
    void eliminarMarca_DeberiaLanzarErrorSiNoExiste() {
        when(marcaRepository.existsById(5L)).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> marcaService.eliminarMarca(5L));

        assertTrue(ex.getMessage().contains("No se encontró la marca"));
    }

    // 🧩 Listar marcas
    @Test
    void listarMarcas_DeberiaRetornarListaCorrectamente() {
        Marca m1 = new Marca();
        m1.setId(1L);
        m1.setNombre("Coca-Cola");

        Marca m2 = new Marca();
        m2.setId(2L);
        m2.setNombre("Pepsi");

        when(marcaRepository.findAll()).thenReturn(Arrays.asList(m1, m2));

        List<MarcaResponseDTO> result = marcaService.listarMarcas();

        assertEquals(2, result.size());
        assertEquals("Pepsi", result.get(1).getNombre());
        verify(marcaRepository).findAll();
    }

    // 🧩 Obtener marca por ID
    @Test
    void obtenerMarcaPorId_DeberiaRetornarCorrectamente() {
        Marca m = new Marca();
        m.setId(1L);
        m.setNombre("Coca-Cola");

        when(marcaRepository.findById(1L)).thenReturn(Optional.of(m));

        MarcaResponseDTO result = marcaService.obtenerMarcaPorId(1L);

        assertEquals("Coca-Cola", result.getNombre());
        verify(marcaRepository).findById(1L);
    }

    @Test
    void obtenerMarcaPorId_DeberiaLanzarErrorSiNoExiste() {
        when(marcaRepository.findById(5L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> marcaService.obtenerMarcaPorId(5L));

        assertTrue(ex.getMessage().contains("No se encontró la marca"));
    }
}

