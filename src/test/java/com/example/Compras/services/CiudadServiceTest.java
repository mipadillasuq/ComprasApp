package com.example.Compras.services;

import com.example.Compras.dto.CiudadRequestDTO;
import com.example.Compras.dto.CiudadResponseDTO;
import com.example.Compras.entities.Ciudad;
import com.example.Compras.repositories.CiudadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CiudadServiceTest {

    @Mock
    private CiudadRepository ciudadRepository;

    @InjectMocks
    private CiudadService ciudadService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crearCiudad_DeberiaCrearCorrectamente() {
        CiudadRequestDTO dto = new CiudadRequestDTO();
        dto.setNombre("Bogotá");

        when(ciudadRepository.findByNombreIgnoreCase("Bogotá")).thenReturn(Optional.empty());
        when(ciudadRepository.save(any(Ciudad.class)))
                .thenAnswer(invocation -> {
                    Ciudad ciudad = invocation.getArgument(0);
                    ciudad.setId(1);
                    return ciudad;
                });

        CiudadResponseDTO result = ciudadService.crearCiudad(dto);

        assertNotNull(result);
        assertEquals("Bogotá", result.getNombre());
        verify(ciudadRepository).save(any(Ciudad.class));
    }

    @Test
    void crearCiudad_DeberiaLanzarExcepcionSiExiste() {
        CiudadRequestDTO dto = new CiudadRequestDTO();
        dto.setNombre("Medellín");

        when(ciudadRepository.findByNombreIgnoreCase("Medellín"))
                .thenReturn(Optional.of(new Ciudad()));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> ciudadService.crearCiudad(dto));

        assertTrue(exception.getMessage().contains("La ciudad ya existe"));
    }

    @Test
    void listarCiudades_DeberiaRetornarListaDeDTOs() {
        Ciudad c1 = new Ciudad("Bogotá");
        c1.setId(1);
        Ciudad c2 = new Ciudad("Cali");
        c2.setId(2);

        when(ciudadRepository.findAll()).thenReturn(Arrays.asList(c1, c2));

        List<CiudadResponseDTO> lista = ciudadService.listarCiudades();

        assertEquals(2, lista.size());
        assertEquals("Cali", lista.get(1).getNombre());
        verify(ciudadRepository).findAll();
    }

    @Test
    void obtenerCiudad_DeberiaRetornarCiudadExistente() {
        Ciudad c = new Ciudad("Pereira");
        c.setId(3);

        when(ciudadRepository.findById(3)).thenReturn(Optional.of(c));

        CiudadResponseDTO dto = ciudadService.obtenerCiudad(3);

        assertEquals("Pereira", dto.getNombre());
        verify(ciudadRepository).findById(3);
    }

    @Test
    void obtenerCiudad_DeberiaLanzarExcepcionSiNoExiste() {
        when(ciudadRepository.findById(10)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> ciudadService.obtenerCiudad(10));

        assertTrue(exception.getMessage().contains("Ciudad no encontrada"));
    }

    @Test
    void actualizarCiudad_DeberiaActualizarCorrectamente() {
        CiudadRequestDTO dto = new CiudadRequestDTO();
        dto.setNombre("Manizales");

        Ciudad existente = new Ciudad("Bogotá");
        existente.setId(5);

        when(ciudadRepository.findById(5)).thenReturn(Optional.of(existente));
        when(ciudadRepository.findByNombreIgnoreCase("Manizales")).thenReturn(Optional.empty());
        when(ciudadRepository.save(any(Ciudad.class))).thenReturn(existente);

        CiudadResponseDTO result = ciudadService.actualizarCiudad(5, dto);

        assertEquals("Manizales", result.getNombre());
        verify(ciudadRepository).save(any(Ciudad.class));
    }

    @Test
    void actualizarCiudad_DeberiaLanzar404SiNoExiste() {
        CiudadRequestDTO dto = new CiudadRequestDTO();
        dto.setNombre("Cartagena");

        when(ciudadRepository.findById(20)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> ciudadService.actualizarCiudad(20, dto));

        assertTrue(exception.getMessage().contains("Ciudad no encontrada"));
    }

    @Test
    void eliminarCiudad_DeberiaEliminarSiExiste() {
        when(ciudadRepository.existsById(1)).thenReturn(true);

        ciudadService.eliminarCiudad(1);

        verify(ciudadRepository).deleteById(1);
    }

    @Test
    void eliminarCiudad_DeberiaLanzar404SiNoExiste() {
        when(ciudadRepository.existsById(1)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> ciudadService.eliminarCiudad(1));

        assertTrue(exception.getMessage().contains("Ciudad no encontrada"));
    }
}

