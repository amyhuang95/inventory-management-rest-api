package example.inventory_management_rest_api.model;

import jakarta.persistence.*;

import java.io.Serializable;

/**
 * The Inventory data model class corresponds to entity and table inventory.
 */

@Entity
@Table(name = "inventory")
public class Inventory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "unit price")
    private Double unitPrice;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "category")
    private String category;

    @Column(name = "on-sale")
    private boolean onSale;

    public Inventory() {
    }

    public Inventory(String name, String description, Double unitPrice, int quantity, String category,
                     boolean onSale) {
        this.name = name;
        this.description = description;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.category = category;
        this.onSale = onSale;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isOnSale() {
        return onSale;
    }

    public void setOnSale(boolean onSale) {
        this.onSale = onSale;
    }

    @Override
    public String toString() {
        return "Item id=[" + id + "], name=" + name + ", desc=" + description + ", price=" + unitPrice + "quant=" + quantity + ", onSale=" + onSale;
    }
}
