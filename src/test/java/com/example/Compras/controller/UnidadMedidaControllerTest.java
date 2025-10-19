package com.example.Compras.controller;

import com.example.Compras.dto.UnidadMedidaRequestDTO;
import com.example.Compras.dto.UnidadMedidaResponseDTO;
import com.example.Compras.services.UnidadMedidaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UnidadMedidaController.class)
@AutoConfigureMockMvc(addFilters = false)
class UnidadMedidaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
    @MockBean
    private com.example.Compras.security.JwtAuthFilter jwtAuthFilter;

    @SuppressWarnings("removal")
    @MockBean
    private com.example.Compras.security.JwtUtil jwtUtil;

    @MockBean
    private UnidadMedidaService unidadMedidaService;

    @Autowired
    private ObjectMapper objectMapper;

    private UnidadMedidaRequestDTO requestDTO;
    private UnidadMedidaResponseDTO responseDTO1;
    private UnidadMedidaResponseDTO responseDTO2;

    @BeforeEach
    void setUp() {
        requestDTO = new UnidadMedidaRequestDTO();
        requestDTO.setNombre("Kilogramo");

        // ✅ Se usa Long (1L y 2L)
        responseDTO1 = new UnidadMedidaResponseDTO(1L, "Kilogramo");
        responseDTO2 = new UnidadMedidaResponseDTO(2L, "Litro");
    }

    @Test
    void crearUnidad_DeberiaRetornar200YUnidadCreada() throws Exception {
        Mockito.when(unidadMedidaService.crearUnidad(Mockito.any(UnidadMedidaRequestDTO.class)))
                .thenReturn(responseDTO1);

        mockMvc.perform(post("/unidad-medida")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Kilogramo")));
    }

    @Test
    void listarTodas_DeberiaRetornar200YListaDeUnidades() throws Exception {
        List<UnidadMedidaResponseDTO> lista = Arrays.asList(responseDTO1, responseDTO2);
        Mockito.when(unidadMedidaService.listarTodas()).thenReturn(lista);

        mockMvc.perform(get("/unidad-medida"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombre", is("Kilogramo")))
                .andExpect(jsonPath("$[1].nombre", is("Litro")));
    }

    @Test
    void buscarPorId_DeberiaRetornar200YUnidad() throws Exception {
        Mockito.when(unidadMedidaService.buscarPorId(1)).thenReturn(responseDTO1);

        mockMvc.perform(get("/unidad-medida/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Kilogramo")));
    }

    @Test
    void actualizarUnidad_DeberiaRetornar200YUnidadActualizada() throws Exception {
        UnidadMedidaResponseDTO actualizado = new UnidadMedidaResponseDTO(1L, "Gramo");

        Mockito.when(unidadMedidaService.actualizarUnidad(Mockito.eq(1), Mockito.any(UnidadMedidaRequestDTO.class)))
                .thenReturn(actualizado);

        mockMvc.perform(put("/unidad-medida/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Gramo")));
    }

    @Test
    void eliminarUnidad_DeberiaRetornar204() throws Exception {
        mockMvc.perform(delete("/unidad-medida/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(unidadMedidaService).eliminarUnidad(1);
    }
}



