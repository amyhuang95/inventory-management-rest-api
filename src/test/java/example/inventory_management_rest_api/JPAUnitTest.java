package example.inventory_management_rest_api;

import example.inventory_management_rest_api.model.Inventory;
import example.inventory_management_rest_api.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Testing JPA and custom finder methods.
 */

@DataJpaTest
class JPAUnitTest {

    @Autowired
    InventoryRepository repository;
    @Autowired
    private TestEntityManager entityManager;
    private Inventory item1, item2, item3, item4;

    /**
     * Set up test cases before each test, reducing duplicate code in each test.
     */
    @BeforeEach
    void setUp() {
        item1 = new Inventory("MP3", "Music player", 5.25, 10, "Electronics", true);
        entityManager.persist(item1);

        item2 = new Inventory("Laptop", "Portable computer", 100.0, 5, "Electronics", false);
        entityManager.persist(item2);

        item3 = new Inventory("Ear pods", "Ear phone with charging cases", 50.0, 7, "Electronics", true);
        entityManager.persist(item3);

        item4 = new Inventory("Laptop Charger", "Charger for a laptop", 15.0, 4, "Electronics", true);
        entityManager.persist(item4);

    }

    /**
     * Test that when the repository is empty, it should find nothing.
     */
    @Test
    public void should_find_noting_if_repo_empty() {
        repository.deleteAll();
        Iterable<Inventory> inventory = repository.findAll();
        assertThat(inventory).isEmpty();
    }

    /**
     * Test when creating a new inventory, all the values match with the given values.
     */
    @Test
    public void should_store_inventory() {
        assertThat(item1).hasFieldOrPropertyWithValue("name", "MP3");
        assertThat(item1).hasFieldOrPropertyWithValue("unitPrice", 5.25);
        assertThat(item1).hasFieldOrPropertyWithValue("quantity", 10);
        assertThat(item1).hasFieldOrPropertyWithValue("description", "Music player");
        assertThat(item1).hasFieldOrPropertyWithValue("category", "Electronics");
        assertThat(item1).hasFieldOrPropertyWithValue("onSale", true);
    }

    /**
     * Test that it finds all inventory items.
     */
    @Test
    public void should_find_all_inventory() {
        Iterable<Inventory> inventory = repository.findAll();
        assertThat(inventory).hasSize(4).contains(item1, item2, item3, item4);

    }

    /**
     * Test that it finds inventory by their id.
     */
    @Test
    public void should_find_inventory_by_id() {
        Inventory inventoryFound = repository.findById(item2.getId()).get();
        assertThat(inventoryFound).isEqualTo(item2);
    }

    /**
     * Test that it finds all inventory items on sale.
     */
    public void should_find_onSale_inventory() {
        Iterable<Inventory> inventory = repository.findByOnSale(true);
        assertThat(inventory).hasSize(2).contains(item1, item3);
    }

    /**
     * Test that it finds all inventory items containing a given text in the name
     */
    public void should_find_inventory_by_name_contain_text() {
        Iterable<Inventory> inventory = repository.findByNameContaining("Laptop");
        assertThat(inventory).hasSize(2).contains(item2, item4);
    }

    /**
     * Test that it updates inventory by id.
     */
    public void should_update_inventory_by_id() {
        Inventory updateInventory = new Inventory("Classic MP3", "Classic music player", 6.0, 1, "Electronics", false);

        // Get item (id: 1) from the repository
        Inventory item = repository.findById(item1.getId()).get();
        // Change item (id:1) properties to updatedInventory
        item.setName(updateInventory.getName());
        item.setUnitPrice(updateInventory.getUnitPrice());
        item.setQuantity(updateInventory.getQuantity());
        item.setDescription(updateInventory.getDescription());
        item.setCategory(updateInventory.getCategory());
        item.setOnSale(updateInventory.isOnSale());
        // Save the changes
        repository.save(item);

        // Get item (id:1) from the repository
        Inventory checkInventory = repository.findById(item1.getId()).get();
        // Check each field whether it is the same as the updateInventory's properties
        assertThat(checkInventory.getId()).isEqualTo(item1.getId());
        assertThat(checkInventory.getName()).isEqualTo(updateInventory.getName());
        assertThat(checkInventory.getUnitPrice()).isEqualTo(updateInventory.getUnitPrice());
        assertThat(checkInventory.getQuantity()).isEqualTo(updateInventory.getQuantity());
        assertThat(checkInventory.getDescription()).isEqualTo(updateInventory.getDescription());
        assertThat(checkInventory.getCategory()).isEqualTo(updateInventory.getCategory());
        assertThat(checkInventory.isOnSale()).isEqualTo(updateInventory.isOnSale());
    }

    /**
     * Test that it deletes inventory by their id.
     */
    @Test
    public void should_delete_inventory_by_id() {
        repository.deleteById(item3.getId());
        Iterable<Inventory> inventory = repository.findAll();
        assertThat(inventory).hasSize(3).contains(item1, item2, item4);
    }

    /**
     * Test it deletes all inventory
     */
    @Test
    public void should_delete_all() {
        repository.deleteAll();
        assertThat(repository.findAll()).isEmpty();
    }


}