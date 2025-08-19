package uy.edu.ucu.inventario.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Entity representing the stock of a product in a specific deposit.
 */
@Entity
@Table(name = "stock")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "deposit_id", nullable = false)
    private Deposit deposit;

    @Column(nullable = false)
    private Integer quantity;

    // === Constructors ===

    public Stock() {}

    public Stock(Product product, Deposit deposit, int quantity) {
        this.product = product;
        this.deposit = deposit;
        this.quantity = quantity;
    }

    // === Getters & Setters ===

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Deposit getDeposit() {
        return deposit;
    }

    public void setDeposit(Deposit deposit) {
        this.deposit = deposit;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}