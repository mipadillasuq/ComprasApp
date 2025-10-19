package com.example.Compras.services;

import com.example.Compras.entities.Producto;
import com.example.Compras.repositories.ProductoRepository;
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

class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // 🧩 getAll()
    @Test
    void getAll_DeberiaRetornarListaDeProductos() {
        Producto p1 = new Producto();
        p1.setId(1L);
        p1.setNombre("Arroz");

        Producto p2 = new Producto();
        p2.setId(2L);
        p2.setNombre("Aceite");

        when(productoRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Producto> result = productoService.getAll();

        assertEquals(2, result.size());
        assertEquals("Aceite", result.get(1).getNombre());
        verify(productoRepository).findAll();
    }

    // 🧩 getProductoById()
    @Test
    void getProductoById_DeberiaRetornarProductoSiExiste() {
        Producto p = new Producto();
        p.setId(1L);
        p.setNombre("Azúcar");

        when(productoRepository.findById(1L)).thenReturn(Optional.of(p));

        Optional<Producto> result = productoService.getProductoById(1L);

        assertTrue(result.isPresent());
        assertEquals("Azúcar", result.get().getNombre());
        verify(productoRepository).findById(1L);
    }

    @Test
    void getProductoById_DeberiaRetornarVacioSiNoExiste() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Producto> result = productoService.getProductoById(99L);

        assertFalse(result.isPresent());
        verify(productoRepository).findById(99L);
    }

    // 🧩 guardar()
    @Test
    void guardar_DeberiaGuardarYRetornarProducto() {
        Producto p = new Producto();
        p.setNombre("Café");

        when(productoRepository.save(p)).thenReturn(p);

        Producto result = productoService.guardar(p);

        assertEquals("Café", result.getNombre());
        verify(productoRepository).save(p);
    }

    // 🧩 eliminar()
    @Test
    void eliminar_DeberiaLlamarDeleteById() {
        doNothing().when(productoRepository).deleteById(1L);

        productoService.eliminar(1L);

        verify(productoRepository).deleteById(1L);
    }
}

