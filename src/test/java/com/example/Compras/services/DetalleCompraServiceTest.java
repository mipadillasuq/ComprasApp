package com.example.Compras.services;

import com.example.Compras.entities.DetalleCompra;
import com.example.Compras.repositories.DetalleCompraRepository;
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

class DetalleCompraServiceTest {

    @Mock
    private DetalleCompraRepository detalleCompraRepository;

    @InjectMocks
    private DetalleCompraService detalleCompraService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAll_DeberiaRetornarListaDeDetalles() {
        DetalleCompra d1 = new DetalleCompra();
        d1.setIdDetalleCompra(1L);
        DetalleCompra d2 = new DetalleCompra();
        d2.setIdDetalleCompra(2L);

        when(detalleCompraRepository.findAll()).thenReturn(Arrays.asList(d1, d2));

        List<DetalleCompra> result = detalleCompraService.getAll();

        assertEquals(2, result.size());
        verify(detalleCompraRepository, times(1)).findAll();
    }

    @Test
    void getDetalleCompraById_DeberiaRetornarDetalleSiExiste() {
        DetalleCompra detalle = new DetalleCompra();
        detalle.setIdDetalleCompra(5L);

        when(detalleCompraRepository.findById(5L)).thenReturn(Optional.of(detalle));

        Optional<DetalleCompra> result = detalleCompraService.getDetalleCompraById(5L);

        assertTrue(result.isPresent());
        assertEquals(5L, result.get().getIdDetalleCompra());
        verify(detalleCompraRepository).findById(5L);
    }

    @Test
    void getDetalleCompraById_DeberiaRetornarVacioSiNoExiste() {
        when(detalleCompraRepository.findById(9L)).thenReturn(Optional.empty());

        Optional<DetalleCompra> result = detalleCompraService.getDetalleCompraById(9L);

        assertTrue(result.isEmpty());
        verify(detalleCompraRepository).findById(9L);
    }

    @Test
    void save_DeberiaGuardarYRetornarDetalle() {
        DetalleCompra detalle = new DetalleCompra();
        detalle.setIdDetalleCompra(1L);

        when(detalleCompraRepository.save(detalle)).thenReturn(detalle);

        DetalleCompra result = detalleCompraService.save(detalle);

        assertEquals(1L, result.getIdDetalleCompra());
        verify(detalleCompraRepository).save(detalle);
    }

    @Test
    void eliminar_DeberiaLlamarDeleteById() {
        detalleCompraService.eliminar(3L);

        verify(detalleCompraRepository, times(1)).deleteById(3L);
    }
}

