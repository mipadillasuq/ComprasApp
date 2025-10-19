package com.example.Compras.controller;

import com.example.Compras.dto.ProveedorTelefonoRequestDTO;
import com.example.Compras.dto.ProveedorTelefonoResponseDTO;
import com.example.Compras.services.ProveedorTelefonoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.reflect.FieldUtils;
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

@WebMvcTest(ProveedorTelefonoController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProveedorTelefonoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
    @MockBean
    private com.example.Compras.security.JwtAuthFilter jwtAuthFilter;

    @SuppressWarnings("removal")
    @MockBean
    private com.example.Compras.security.JwtUtil jwtUtil;

    @MockBean
    private ProveedorTelefonoService proveedorTelefonoService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProveedorTelefonoRequestDTO requestDTO;
    private ProveedorTelefonoResponseDTO responseDTO1;
    private ProveedorTelefonoResponseDTO responseDTO2;

    @BeforeEach
    void setUp() throws Exception {
        requestDTO = new ProveedorTelefonoRequestDTO();

        // Detecta si los nombres de campo son proveedorId o idProveedor
        if (FieldUtils.getDeclaredField(ProveedorTelefonoRequestDTO.class, "proveedorId", true) != null) {
            FieldUtils.writeField(requestDTO, "proveedorId", 1L, true);
            FieldUtils.writeField(requestDTO, "telefonoId", 5L, true);
        } else {
            FieldUtils.writeField(requestDTO, "idProveedor", 1L, true);
            FieldUtils.writeField(requestDTO, "idTelefono", 5L, true);
        }

        // Respuestas simuladas
        responseDTO1 = new ProveedorTelefonoResponseDTO(
                10L, 1L, "Proveedor A", 5L, "3001112233"
        );

        responseDTO2 = new ProveedorTelefonoResponseDTO(
                11L, 1L, "Proveedor A", 6L, "3002223344"
        );
    }

    @Test
    void crearRelacion_DeberiaRetornar200YRelacionCreada() throws Exception {
        Mockito.when(proveedorTelefonoService.crearRelacion(Mockito.any(ProveedorTelefonoRequestDTO.class)))
                .thenReturn(responseDTO1);

        mockMvc.perform(post("/proveedor-telefono")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(10)))
                // 💡 CAMBIO AQUÍ: el campo real es "nombreProveedor"
                .andExpect(jsonPath("$.nombreProveedor", is("Proveedor A")))
                .andExpect(jsonPath("$.numeroTelefono", is("3001112233")));
    }

    @Test
    void listarPorProveedor_DeberiaRetornar200YListaDeRelaciones() throws Exception {
        List<ProveedorTelefonoResponseDTO> lista = Arrays.asList(responseDTO1, responseDTO2);
        Mockito.when(proveedorTelefonoService.listarPorProveedor(1L)).thenReturn(lista);

        mockMvc.perform(get("/proveedor-telefono/proveedor/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].numeroTelefono", is("3001112233")))
                .andExpect(jsonPath("$[1].numeroTelefono", is("3002223344")));
    }

    @Test
    void eliminarRelacion_DeberiaRetornar204() throws Exception {
        mockMvc.perform(delete("/proveedor-telefono/10"))
                .andExpect(status().isNoContent());

        Mockito.verify(proveedorTelefonoService).eliminarRelacion(10L);
    }
}
