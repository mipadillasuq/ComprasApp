package com.example.Compras.controller;

import com.example.Compras.dto.ProveedorRequestDTO;
import com.example.Compras.dto.ProveedorResponseDTO;
import com.example.Compras.dto.TelefonoRequestDTO;
import com.example.Compras.services.ProveedorService;
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

@WebMvcTest(ProveedorController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProveedorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
    @MockBean
    private com.example.Compras.security.JwtAuthFilter jwtAuthFilter;

    @SuppressWarnings("removal")
    @MockBean
    private com.example.Compras.security.JwtUtil jwtUtil;

    @MockBean
    private ProveedorService proveedorService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProveedorResponseDTO proveedor1;
    private ProveedorResponseDTO proveedor2;
    private ProveedorRequestDTO proveedorRequest;
    private TelefonoRequestDTO telefonoRequest;

    @BeforeEach
    void setUp() {
        proveedor1 = new ProveedorResponseDTO();
        proveedor1.setIdProveedor(1L);
        proveedor1.setNombre("Proveedor A");
        proveedor1.setEmail("proveedora@email.com");

        proveedor2 = new ProveedorResponseDTO();
        proveedor2.setIdProveedor(2L);
        proveedor2.setNombre("Proveedor B");
        proveedor2.setEmail("proveedorb@email.com");

        // ✅ Como tu DTO exige idProveedor con @NotNull, debemos incluirlo aquí
        proveedorRequest = new ProveedorRequestDTO();
        proveedorRequest.setIdProveedor(3L);
        proveedorRequest.setNombre("Proveedor C");
        proveedorRequest.setCiudadId(10L);
        proveedorRequest.setDireccion("Calle 123");
        proveedorRequest.setEmail("proveedorc@email.com");
        proveedorRequest.setEstado(true);

        telefonoRequest = new TelefonoRequestDTO();
        telefonoRequest.setNumero("3001234567");
    }

    @Test
    void listar_DeberiaRetornar200YListaDeProveedores() throws Exception {
        List<ProveedorResponseDTO> proveedores = Arrays.asList(proveedor1, proveedor2);
        Mockito.when(proveedorService.listarTodos()).thenReturn(proveedores);

        mockMvc.perform(get("/proveedores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombre", is("Proveedor A")))
                .andExpect(jsonPath("$[1].nombre", is("Proveedor B")));
    }

    @Test
    void obtenerPorId_DeberiaRetornar200YProveedor() throws Exception {
        Mockito.when(proveedorService.getProveedor(1L)).thenReturn(proveedor1);

        mockMvc.perform(get("/proveedores/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Proveedor A")));
    }

    @Test
    void crear_DeberiaRetornar200YProveedorCreado() throws Exception {
        Mockito.when(proveedorService.crearProveedor(Mockito.any(ProveedorRequestDTO.class)))
                .thenReturn(proveedor1);

        mockMvc.perform(post("/proveedores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(proveedorRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Proveedor A")));
    }

    @Test
    void eliminar_DeberiaRetornar200YMensaje() throws Exception {
        mockMvc.perform(delete("/proveedores/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Proveedor eliminado"));

        Mockito.verify(proveedorService).eliminarProveedor(1L);
    }

    @Test
    void agregarTelefono_DeberiaRetornar200YProveedorActualizado() throws Exception {
        ProveedorResponseDTO actualizado = new ProveedorResponseDTO();
        actualizado.setIdProveedor(1L);
        actualizado.setNombre("Proveedor A");
        actualizado.setEmail("proveedora@email.com");

        Mockito.when(proveedorService.agregarTelefono(Mockito.eq(1L), Mockito.any(TelefonoRequestDTO.class)))
                .thenReturn(actualizado);

        mockMvc.perform(post("/proveedores/1/telefonos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(telefonoRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Proveedor A")));
    }

    @Test
    void quitarTelefono_DeberiaRetornar200YProveedorActualizado() throws Exception {
        ProveedorResponseDTO actualizado = new ProveedorResponseDTO();
        actualizado.setIdProveedor(1L);
        actualizado.setNombre("Proveedor A");
        actualizado.setEmail("proveedora@email.com");

        Mockito.when(proveedorService.quitarTelefono(1L, 10L)).thenReturn(actualizado);

        mockMvc.perform(delete("/proveedores/1/telefonos/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Proveedor A")));
    }
}
