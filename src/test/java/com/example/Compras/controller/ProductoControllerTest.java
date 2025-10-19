package com.example.Compras.controller;

import com.example.Compras.entities.Producto;
import com.example.Compras.services.ProductoService;
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
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductoControllerTest {

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
    private ProductoService productoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Producto producto1;
    private Producto producto2;

    @BeforeEach
    void setUp() {
        producto1 = new Producto();
        producto1.setId(1L);
        producto1.setNombre("Laptop");
        producto1.setPrecio(1200.0);

        producto2 = new Producto();
        producto2.setId(2L);
        producto2.setNombre("Mouse");
        producto2.setPrecio(50.0);
    }

    @Test
    void listar_DeberiaRetornar200YListaDeProductos() throws Exception {
        List<Producto> productos = Arrays.asList(producto1, producto2);
        Mockito.when(productoService.getAll()).thenReturn(productos);

        mockMvc.perform(get("/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombre", is("Laptop")))
                .andExpect(jsonPath("$[1].nombre", is("Mouse")));
    }

    @Test
    void obtenerPorId_DeberiaRetornar200YProducto() throws Exception {
        Mockito.when(productoService.getProductoById(1L)).thenReturn(Optional.of(producto1));

        mockMvc.perform(get("/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Laptop")));
    }

    @Test
    void obtenerPorId_DeberiaRetornar404SiNoExiste() throws Exception {
        Mockito.when(productoService.getProductoById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/productos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crear_DeberiaRetornar200YProductoCreado() throws Exception {
        Mockito.when(productoService.guardar(Mockito.any(Producto.class))).thenReturn(producto1);

        mockMvc.perform(post("/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Laptop")));
    }

    @Test
    void actualizar_DeberiaRetornar200YProductoActualizado() throws Exception {
        Producto actualizado = new Producto();
        actualizado.setId(1L);
        actualizado.setNombre("Laptop Gamer");
        actualizado.setPrecio(1500.0);

        Mockito.when(productoService.getProductoById(1L)).thenReturn(Optional.of(producto1));
        Mockito.when(productoService.guardar(Mockito.any(Producto.class))).thenReturn(actualizado);

        mockMvc.perform(put("/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Laptop Gamer")));
    }

    @Test
    void actualizar_DeberiaRetornar404SiNoExiste() throws Exception {
        Mockito.when(productoService.getProductoById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/productos/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto1)))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminar_DeberiaRetornar204() throws Exception {
        Mockito.when(productoService.getProductoById(1L)).thenReturn(Optional.of(producto1));

        mockMvc.perform(delete("/productos/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(productoService).eliminar(1L);
    }

    @Test
    void eliminar_DeberiaRetornar404SiNoExiste() throws Exception {
        Mockito.when(productoService.getProductoById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/productos/99"))
                .andExpect(status().isNotFound());
    }
}
