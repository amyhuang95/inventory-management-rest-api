package example.inventory_management_rest_api.repository;

import example.inventory_management_rest_api.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * This interface extends JpaRepository for CRUD methods and custom finder methods.
 * JpaRepository supports save(), findOne(), findById(), findAll(), count(), delete(), and deleteById().
 */

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    /**
     * Custom finder: Get Inventory items by whether it is on sales.
     *
     * @param onSale the status of the item
     * @return list of inventory items of the specified sale status
     */
    List<Inventory> findByOnSale(boolean onSale);

    /**
     * Custom finder: Get Inventory items whose name contains specified text.
     *
     * @param name text to search in the Inventory item name
     * @return list of inventory items including the specified text in their names
     */
    List<Inventory> findByNameContaining(String name);
}
