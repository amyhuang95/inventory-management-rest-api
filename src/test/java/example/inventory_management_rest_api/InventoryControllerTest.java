package example.inventory_management_rest_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.inventory_management_rest_api.controller.InventoryController;
import example.inventory_management_rest_api.model.Inventory;
import example.inventory_management_rest_api.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test the controller on:
 * (1) whether an HTTP request is mapped to correct endpoints;
 * (2) whether path variables are mapped correctly;
 * (3) whether request body and response body work correctly.
 */
@WebMvcTest(InventoryController.class)
class InventoryControllerTest {

    // Create and inject a mock of the InventoryRepository
    @MockBean
    private InventoryRepository repository;

    // To fake HTTP requests
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Inventory item1, item2, item3;

    @BeforeEach
    void setUp() {
        item1 = new Inventory("MP3", "Music player", 5.25, 10, "Electronics", true);
        item2 = new Inventory("Laptop", "Portable computer", 100.0, 5, "Electronics", false);
        item3 = new Inventory("Laptop Charger", "Charger for a laptop", 15.0, 4, "Electronics", true);
    }

    /***
     * Test API endpoint for the method: POST /api/inventory
     * @throws Exception
     */
    @Test
    void shouldCreateInventory() throws Exception {
        mockMvc.perform(post("/api/inventory").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item1)))
                .andExpect(status().isCreated())
                .andDo(print());
    }


    /**
     * Test API endpoint for the method: GET /api/inventory/[:id]
     *
     * @throws Exception
     */
    @Test
    void shouldReturnInventory() throws Exception {
        when(repository.findById(item1.getId())).thenReturn(Optional.of(item1));
        mockMvc.perform(get("/api/inventory/{id}", item1.getId())).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(item1.getId()))
                .andExpect(jsonPath("$.name").value(item1.getName()))
                .andExpect(jsonPath("$.unitPrice").value(item1.getUnitPrice()))
                .andExpect(jsonPath("$.quantity").value(item1.getQuantity()))
                .andExpect(jsonPath("$.description").value(item1.getDescription()))
                .andExpect(jsonPath("$.category").value(item1.getCategory()))
                .andExpect(jsonPath("$.onSale").value(item1.isOnSale()))
                .andDo(print());
    }

    /**
     * Test API endpoint for the method: GET /api/inventory/[:id]
     * when there is nothing in the repository.
     * Should return 404.
     *
     * @throws Exception
     */
    @Test
    void shouldReturnNotFoundInventory() throws Exception {
        // Remove all inventory items set up before the test
        repository.deleteAll();

        when(repository.findById(item1.getId())).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/inventory/{id}", item1.getId()))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    /**
     * Test API endpoint for the method: GET /api/inventory
     * when there are some items in the repository.
     *
     * @throws Exception
     */
    @Test
    void shouldReturnListOfInventory() throws Exception {
        List<Inventory> items = new ArrayList<>(Arrays.asList(item1, item2, item3));

        when(repository.findAll()).thenReturn(items);
        mockMvc.perform(get("/api/inventory"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(items.size()))
                .andDo(print());
    }

    /**
     * Test API endpoint for the method: GET /api/inventory?name=[:text]
     * when there are matches in the repository.
     *
     * @throws Exception
     */
    @Test
    void shouldReturnListOfInventoryWithFilter() throws Exception {
        List<Inventory> items = new ArrayList<>(Arrays.asList(item2, item3));

        String name = "Laptop";
        MultiValueMap<String, String> filter = new LinkedMultiValueMap<>();
        filter.add("name", name);

        when(repository.findByNameContaining(name)).thenReturn(items);
        mockMvc.perform(get("/api/inventory").params(filter))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(items.size()))
                .andDo(print());
    }

    /**
     * Test API endpoint for the method: GET /api/inventory?name=[:text]
     * when there is no match in the repository
     *
     * @throws Exception
     */
    @Test
    void shouldReturnNoContentWhenFilter() throws Exception {
        String name = "apple";
        MultiValueMap<String, String> filter = new LinkedMultiValueMap<>();
        filter.add("name", name);

        List<Inventory> items = Collections.emptyList();
        when(repository.findByNameContaining(name)).thenReturn(items);
        mockMvc.perform(get("/api/inventory").params(filter))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    /**
     * Test API endpoint for the method: PUT /api/inventory/[:id]
     * when the inventory item to be updated exists.
     *
     * @throws Exception
     */
    @Test
    void shouldUpdateInventory() throws Exception {
        Inventory updateItem1 = new Inventory("MP4", "More advanced music player", 7.0, 1, "Electronics", false);

        when(repository.findById(item1.getId())).thenReturn(Optional.of(item1));
        when(repository.save(any(Inventory.class))).thenReturn(updateItem1);
        mockMvc.perform(put("/api/inventory/{id}", item1.getId()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateItem1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updateItem1.getName()))
                .andExpect(jsonPath("$.unitPrice").value(updateItem1.getUnitPrice()))
                .andExpect(jsonPath("$.quantity").value(updateItem1.getQuantity()))
                .andExpect(jsonPath("$.description").value(updateItem1.getDescription()))
                .andExpect(jsonPath("$.category").value(updateItem1.getCategory()))
                .andExpect(jsonPath("$.onSale").value(updateItem1.isOnSale()))
                .andDo(print());
    }

    /**
     * Test API endpoint for the method: PUT /api/inventory/[:id]
     * when the inventory item to be updated does not exist.
     *
     * @throws Exception
     */
    @Test
    void shouldReturnNotFoundUpdateInventory() throws Exception {
        Inventory updateItem1 = new Inventory("MP4", "More advanced music player", 7.0, 1, "Electronics", false);

        when(repository.findById(updateItem1.getId())).thenReturn(Optional.empty());
        when(repository.save(any(Inventory.class))).thenReturn(updateItem1); // might be extra

        mockMvc.perform(put("/api/inventory/{id}", updateItem1.getId()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateItem1)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    /**
     * Test API endpoint for the method: DELETE /api/inventory/[:id]
     *
     * @throws Exception
     */
    @Test
    void shouldDeleteInventory() throws Exception {
        // simulate successful deletion
        doNothing().when(repository).deleteById(item2.getId());

        // perform deletion of requested item
        mockMvc.perform(delete("/api/inventory/{id}", item2.getId()))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    /**
     * Test API endpoint for the method: DELETE /api/inventory
     *
     * @throws Exception
     */
    @Test
    void shouldDeleteAllInventory() throws Exception {
        // mock getting inventory items before deletion
        List<Inventory> items = new ArrayList<>(Arrays.asList(item1, item2, item3));
        when(repository.findAll()).thenReturn(items);

        // perform the deletion
        mockMvc.perform(delete("/api/inventory"))
                .andExpect(status().isNoContent())
                .andDo(print());

        // mock find all to return empty list
        when(repository.findAll()).thenReturn(Collections.emptyList());

    }
}