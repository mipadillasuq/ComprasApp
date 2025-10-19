package com.example.Compras.controller;

import com.example.Compras.entities.DetalleCompra;
import com.example.Compras.services.DetalleCompraService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class DetalleCompraControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DetalleCompraService detalleCompraService;

    @InjectMocks
    private DetalleCompraController detalleCompraController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(detalleCompraController).build();
        objectMapper = new ObjectMapper();
    }

    // 🧩 GET - Listar todos los detalles de compra
    @Test
    void listar_DeberiaRetornarListaDeDetalles() throws Exception {
        DetalleCompra d1 = new DetalleCompra();
        d1.setIdDetalleCompra(1L);
        DetalleCompra d2 = new DetalleCompra();
        d2.setIdDetalleCompra(2L);

        List<DetalleCompra> lista = Arrays.asList(d1, d2);
        when(detalleCompraService.getAll()).thenReturn(lista);

        mockMvc.perform(get("/detalle_compra"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(detalleCompraService).getAll();
    }

    // 🧩 GET - Obtener un detalle por ID
    @Test
    void obtenerPorId_DeberiaRetornarDetalleSiExiste() throws Exception {
        DetalleCompra detalle = new DetalleCompra();
        detalle.setIdDetalleCompra(1L);

        when(detalleCompraService.getDetalleCompraById(1L)).thenReturn(Optional.of(detalle));

        mockMvc.perform(get("/detalle_compra/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idDetalleCompra").value(1L));

        verify(detalleCompraService).getDetalleCompraById(1L);
    }

    @Test
    void obtenerPorId_DeberiaRetornar404SiNoExiste() throws Exception {
        when(detalleCompraService.getDetalleCompraById(10L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/detalle_compra/10"))
                .andExpect(status().isNotFound());

        verify(detalleCompraService).getDetalleCompraById(10L);
    }

    // 🧩 POST - Crear un detalle de compra
    @Test
    void crear_DeberiaRetornarDetalleCreado() throws Exception {
        DetalleCompra request = new DetalleCompra();
        request.setIdDetalleCompra(1L);

        when(detalleCompraService.save(any(DetalleCompra.class))).thenReturn(request);

        mockMvc.perform(post("/detalle_compra")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idDetalleCompra").value(1L));

        verify(detalleCompraService).save(any(DetalleCompra.class));
    }

    // 🧩 DELETE - Eliminar detalle existente
    @Test
    void eliminar_DeberiaRetornar204SiExiste() throws Exception {
        DetalleCompra detalle = new DetalleCompra();
        detalle.setIdDetalleCompra(1L);

        when(detalleCompraService.getDetalleCompraById(1L)).thenReturn(Optional.of(detalle));
        doNothing().when(detalleCompraService).eliminar(1L);

        mockMvc.perform(delete("/detalle_compra/1"))
                .andExpect(status().isNoContent());

        verify(detalleCompraService).eliminar(1L);
    }

    @Test
    void eliminar_DeberiaRetornar404SiNoExiste() throws Exception {
        when(detalleCompraService.getDetalleCompraById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/detalle_compra/99"))
                .andExpect(status().isNotFound());

        verify(detalleCompraService, never()).eliminar(anyLong());
    }
}
