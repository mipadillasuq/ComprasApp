package com.example.Compras.controller;

import com.example.Compras.dto.MarcaRequestDTO;
import com.example.Compras.dto.MarcaResponseDTO;
import com.example.Compras.security.SecurityConfig;
import com.example.Compras.services.MarcaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MarcaController.class)
@AutoConfigureMockMvc(addFilters = false)
class MarcaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
    @MockBean
    private com.example.Compras.security.JwtAuthFilter jwtAuthFilter;

    @SuppressWarnings("removal")
    @MockBean
    private com.example.Compras.security.JwtUtil jwtUtil;
    @SuppressWarnings("removal")
    @MockBean
    private MarcaService marcaService;

    @Autowired
    private ObjectMapper objectMapper;

    private MarcaResponseDTO marca1;
    private MarcaResponseDTO marca2;
    private MarcaRequestDTO marcaRequest;

    @BeforeEach
    void setUp() {
        marca1 = new MarcaResponseDTO(1L, "Nike");
        marca2 = new MarcaResponseDTO(2L, "Adidas");
        marcaRequest = new MarcaRequestDTO();
        marcaRequest.setNombre("Puma");
    }

    @Test
    void listar_DeberiaRetornar200YListaDeMarcas() throws Exception {
        List<MarcaResponseDTO> marcas = Arrays.asList(marca1, marca2);
        Mockito.when(marcaService.listarMarcas()).thenReturn(marcas);

        mockMvc.perform(get("/marcas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombre", is("Nike")))
                .andExpect(jsonPath("$[1].nombre", is("Adidas")));
    }

    @Test
    void obtenerPorId_DeberiaRetornar200YMarca() throws Exception {
        Mockito.when(marcaService.obtenerMarcaPorId(1L)).thenReturn(marca1);

        mockMvc.perform(get("/marcas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Nike")));
    }

    @Test
    void crear_DeberiaRetornar200YMarcaCreada() throws Exception {
        MarcaResponseDTO creada = new MarcaResponseDTO(3L, "Puma");
        Mockito.when(marcaService.crearMarca(Mockito.any(MarcaRequestDTO.class))).thenReturn(creada);

        mockMvc.perform(post("/marcas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(marcaRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Puma")));
    }

    @Test
    void actualizar_DeberiaRetornar200YMarcaActualizada() throws Exception {
        MarcaResponseDTO actualizada = new MarcaResponseDTO(1L, "Nike Updated");
        Mockito.when(marcaService.actualizarMarca(Mockito.eq(1L), Mockito.any(MarcaRequestDTO.class)))
                .thenReturn(actualizada);

        mockMvc.perform(put("/marcas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(marcaRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Nike Updated")));
    }

    @Test
    void eliminar_DeberiaRetornar204() throws Exception {
        mockMvc.perform(delete("/marcas/1"))
                .andExpect(status().isNoContent());
        Mockito.verify(marcaService).eliminarMarca(1L);
    }
}


