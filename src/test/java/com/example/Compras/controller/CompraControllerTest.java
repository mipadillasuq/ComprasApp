package com.example.Compras.controller;

import com.example.Compras.dto.CompraRequestDTO;
import com.example.Compras.dto.CompraResponseDTO;
import com.example.Compras.services.CompraService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CompraControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CompraService compraService;

    @InjectMocks
    private CompraController compraController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(compraController).build();
        objectMapper = new ObjectMapper();
    }

    // 🧩 GET - Obtener una compra por ID
    @Test
    void getCompra_DeberiaRetornarCompraPorId() throws Exception {
        CompraResponseDTO response = new CompraResponseDTO();
        response.setId(1L);
        response.setNumFactura("FAC-001");
        response.setFecha(LocalDate.now());

        when(compraService.getCompraById(1L)).thenReturn(response);

        mockMvc.perform(get("/compras/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.numFactura").value("FAC-001"));

        verify(compraService).getCompraById(1L);
    }

    // 🧩 GET - Listar todas las compras
    @Test
    void getAll_DeberiaRetornarListaDeCompras() throws Exception {
        CompraResponseDTO c1 = new CompraResponseDTO();
        c1.setId(1L);
        c1.setNumFactura("FAC-001");

        CompraResponseDTO c2 = new CompraResponseDTO();
        c2.setId(2L);
        c2.setNumFactura("FAC-002");

        List<CompraResponseDTO> lista = Arrays.asList(c1, c2);
        when(compraService.getAllCompras()).thenReturn(lista);

        mockMvc.perform(get("/compras"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[1].numFactura").value("FAC-002"));

        verify(compraService).getAllCompras();
    }

    // 🧩 POST - Crear una nueva compra
    @Test
    void saveCompra_DeberiaCrearCompraCorrectamente() throws Exception {
        CompraRequestDTO request = new CompraRequestDTO();
        request.setNumFactura("FAC-003");
        request.setIdProveedor(1L);
        request.setIdUsuario(1L);

        CompraResponseDTO response = new CompraResponseDTO();
        response.setId(3L);
        response.setNumFactura("FAC-003");

        when(compraService.crearCompra(any(CompraRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/compras")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.numFactura").value("FAC-003"));

        verify(compraService).crearCompra(any(CompraRequestDTO.class));
    }

    // 🧩 DELETE - Eliminar compra
    @Test
    void eliminarCompra_DeberiaRetornar204() throws Exception {
        doNothing().when(compraService).eliminarCompra(1L);

        mockMvc.perform(delete("/compras/1"))
                .andExpect(status().isNoContent());

        verify(compraService).eliminarCompra(1L);
    }
}

