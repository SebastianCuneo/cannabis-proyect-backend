package uy.edu.ucu.inventario.entity;

import jakarta.persistence.*;
import uy.edu.ucu.inventario.enums.MovementType;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity representing a stock movement (entry, exit or transfer).
 */
@Entity
@Table(name = "stock_movements")
public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MovementType type;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "origin_deposit_id")
    private Deposit originDeposit;

    @ManyToOne
    @JoinColumn(name = "destination_deposit_id")
    private Deposit destinationDeposit;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Método que se ejecuta justo antes de guardar la entidad por primera vez
    @PrePersist
    protected void onCreate() {
        this.date = LocalDate.now();
    }
    
    // Método que se ejecuta justo antes de actualizar la entidad
    @PreUpdate
    protected void onUpdate() {
        this.date = LocalDate.now();
    }

    // === Constructors ===

    public StockMovement() {}

    public StockMovement(
        MovementType type,
        Product product,
        Deposit originDeposit,
        Deposit destinationDeposit,
        int quantity
    ) {
        this.type = type;
        this.product = product;
        this.originDeposit = originDeposit;
        this.destinationDeposit = destinationDeposit;
        this.quantity = quantity;
        this.date = LocalDate.now();
    }

    // === Getters & Setters ===

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MovementType getType() {
        return type;
    }

    public void setType(MovementType type) {
        this.type = type;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Deposit getOriginDeposit() {
        return originDeposit;
    }

    public void setOriginDeposit(Deposit originDeposit) {
        this.originDeposit = originDeposit;
    }

    public Deposit getDestinationDeposit() {
        return destinationDeposit;
    }

    public void setDestinationDeposit(Deposit destinationDeposit) {
        this.destinationDeposit = destinationDeposit;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}