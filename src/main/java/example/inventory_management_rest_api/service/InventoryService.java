package example.inventory_management_rest_api.service;

import java.util.List;
import java.util.Optional;

import example.inventory_management_rest_api.model.Inventory;
import example.inventory_management_rest_api.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;


@Service
@EnableCaching
public class InventoryService {
    @Autowired
    InventoryRepository repository;

    @Cacheable("inventory")
    public List<Inventory> findAll() {
        doLongRunningTask();

        return repository.findAll();
    }

    @Cacheable("inventory")
    public List<Inventory> findByNameContaining(String name) {

        doLongRunningTask();

        return repository.findByNameContaining(name);
    }

    @Cacheable("inventoryItem")
    public Optional<Inventory> findById(long id) {

        doLongRunningTask();

        return repository.findById(id);
    }

    public Inventory save(Inventory inventory) {
        return repository.save(inventory);
    }

    @CacheEvict(value = "inventory", key = "#inventory.id")
    public Inventory update(Inventory inventory) {
        return repository.save(inventory);
    }

    @CacheEvict(value = "inventory", key = "#id")
    public void deleteById(long id) {
        repository.deleteById(id);
    }

    @CacheEvict(value = {"inventory", "inventoryItem", "inventoryOnSale"}, allEntries = true)
    public void deleteAll() {
        repository.deleteAll();
    }

    @Cacheable("inventoryOnSale")
    public List<Inventory> findByOnSale(boolean onSale) {
        doLongRunningTask();

        return repository.findByOnSale(onSale);
    }

    private void doLongRunningTask() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
