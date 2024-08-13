package example.inventory_management_rest_api.controller;

import example.inventory_management_rest_api.model.Inventory;
import example.inventory_management_rest_api.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * This is a RestController which has request mapping methods for RESTful requests
 */

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class InventoryController {

    @Autowired
    InventoryRepository inventoryRepository;

    /**
     * Get all items.
     *
     * @param name text to search in item names
     * @return a list of items that have the given text in their names; return server error if fails
     */
    @GetMapping("/inventory")
    public ResponseEntity<List<Inventory>> getAllInventory(@RequestParam(required = false) String name) {
        try {
            List<Inventory> inventories = new ArrayList<Inventory>();

            // If user did not provide a name, add all items to the list
            if (name == null) {
                inventoryRepository.findAll().forEach(inventories::add);
            } else { // else add the ones containing the given text
                inventoryRepository.findByNameContaining(name).forEach(inventories::add);
            }

            // If the list is empty, return no content
            if (inventories.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(inventories, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get item with the provided id.
     *
     * @param id id to search in Inventory
     * @return inventory item with the given id; return not found if given id not exists
     */
    @GetMapping("inventory/{id}")
    public ResponseEntity<Inventory> getInventoryById(@PathVariable("id") long id) {
        Optional<Inventory> inventoryData = inventoryRepository.findById(id);

        // Check whether the id exists in the Inventory
        if (inventoryData.isPresent()) {
            return new ResponseEntity<>(inventoryData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Add an inventory item into the database.
     *
     * @param inventory inventory item to be added.
     * @return the added inventory if succeeds; return server error if fails
     */
    @PostMapping("/inventory")
    public ResponseEntity<Inventory> createInventory(@RequestBody Inventory inventory) {
        try {
            Inventory _inventory = inventoryRepository
                    .save(new Inventory(inventory.getName(), inventory.getDescription(),
                            inventory.getUnitPrice(), inventory.getQuantity(), inventory.getCategory(),
                            inventory.isOnSale()));
            return new ResponseEntity<>(_inventory, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Update inventory information based on the id of the item.
     *
     * @param id        id of the item to update
     * @param inventory inventory object
     * @return updated inventory or error message if fails
     */
    @PutMapping("/inventory/{id}")
    public ResponseEntity<Inventory> updateInventory(@PathVariable("id") long id, @RequestBody Inventory inventory) {
        Optional<Inventory> inventoryData = inventoryRepository.findById(id);
        if (inventoryData.isPresent()) {
            Inventory _inventory = inventoryData.get();
            _inventory.setName(inventory.getName());
            _inventory.setDescription(inventory.getDescription());
            _inventory.setUnitPrice(inventory.getUnitPrice());
            _inventory.setQuantity(inventory.getQuantity());
            _inventory.setCategory(inventory.getCategory());
            _inventory.setOnSale(inventory.isOnSale());
            return new ResponseEntity<>(inventoryRepository.save(_inventory), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Delete an inventory item of the given id.
     *
     * @param id of the item to be deleted
     * @return response after deletion
     */
    @DeleteMapping("/inventory/{id}")
    public ResponseEntity<HttpStatus> deleteInventory(@PathVariable("id") long id) {
        try {
            inventoryRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete all inventories.
     *
     * @return response after deletion
     */
    @DeleteMapping("/inventory")
    public ResponseEntity<HttpStatus> deleteAllInventory() {
        try {
            inventoryRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get all inventory items that are on sale.
     *
     * @return a list of items onsale or error message if fails
     */
    @GetMapping("/inventory/onsale")
    public ResponseEntity<List<Inventory>> getInventoryOnSale() {
        try {
            List<Inventory> inventories = inventoryRepository.findByOnSale(true);

            if (inventories.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(inventories, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
