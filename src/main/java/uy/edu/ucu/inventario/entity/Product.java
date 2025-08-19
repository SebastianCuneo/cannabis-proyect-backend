package uy.edu.ucu.inventario.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Entity representing a Product.
 * Each product has an associated brand and category.
 */
@Entity
@Table(name = "products")
public class Product {

    // === Embedded class ===

    @Embeddable
    public static class MonetaryValue {
        private String currency;
        private BigDecimal value;

        public MonetaryValue() {}

        public MonetaryValue(String currency, BigDecimal value) {
            this.currency = currency;
            this.value = value;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public BigDecimal getValue() {
            return value;
        }

        public void setValue(BigDecimal value) {
            this.value = value;
        }
        
    }

    // === Attributes ===

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 255)
    private String description;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "currency", column = @Column(name = "purchase_currency")),
        @AttributeOverride(name = "value", column = @Column(name = "purchase_price"))
    })
    private MonetaryValue purchasePrice;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "currency", column = @Column(name = "sale_currency")),
        @AttributeOverride(name = "value", column = @Column(name = "sale_price"))
    })
    private MonetaryValue salePrice;

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private int depositsCount = 0;
    
    // Nueva relación Many-to-Many
    @ManyToMany
    @JoinTable(
        name = "product_deposit", // Nombre de la tabla de unión
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "deposit_id")
    )
    private Set<Deposit> deposits = new HashSet<>();

    // === Constructors ===

    public Product() {}

    public Product(String name, String description, MonetaryValue purchasePrice, MonetaryValue salePrice, Brand brand, Category category) {
        this.name = name;
        this.description = description;
        this.purchasePrice = purchasePrice;
        this.salePrice = salePrice;
        this.brand = brand;
        this.category = category;
        this.depositsCount = 0;
    }

    // === Getters & Setters ===

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public MonetaryValue getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(MonetaryValue purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public MonetaryValue getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(MonetaryValue salePrice) {
        this.salePrice = salePrice;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getDepositsCount() {
        return depositsCount;
    }

    public void setDepositsCount(int depositsCount) {
        this.depositsCount = depositsCount;
    }

    public void incrementDepositsCount() {
        this.depositsCount++;
    }

    public void decrementDepositsCount() {
        if (this.depositsCount > 0) {
            this.depositsCount--;
        }
    }
    // Reemplaza los métodos relacionados con depositsCount
    public Set<Deposit> getDeposits() {
        return deposits;
    }

    public void setDeposits(Set<Deposit> deposits) {
        this.deposits = deposits;
    }
    
    public void addDeposit(Deposit deposit) {
        if (this.deposits.add(deposit)) {
            // Solo incrementa si el depósito no estaba en la lista
            this.depositsCount++;
        }
    }

    public void removeDeposit(Deposit deposit) {
        if (this.deposits.remove(deposit)) {
            // Solo decrementa si el depósito existía
            this.depositsCount--;
        }
    }
}