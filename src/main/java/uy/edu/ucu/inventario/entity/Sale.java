package uy.edu.ucu.inventario.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import uy.edu.ucu.inventario.enums.PaymentMethod;

/**
 * Entity representing a sale.
 */
@Entity
@Table(name = "sales")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private LocalDateTime date;

    @Column(nullable = true)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Column(length = 100)
    private String reseller; // Optional
    
    // Método que se ejecuta justo antes de guardar la entidad por primera vez
    @PrePersist
    protected void onCreate() {
        this.date = LocalDateTime.now();
    }
    
    // Método que se ejecuta justo antes de actualizar la entidad
    @PreUpdate
    protected void onUpdate() {
        this.date = LocalDateTime.now();
    }


    @ManyToMany
    @JoinTable(
        name = "sale_products",
        joinColumns = @JoinColumn(name = "sale_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products;

    @Transient
    private Long totalCount;

    // === Constructors ===

    public Sale() {}

    public Sale(LocalDateTime date, BigDecimal total, PaymentMethod paymentMethod, String reseller, List<Product> products) {
        this.date = date;
        this.total = total;
        this.paymentMethod = paymentMethod;
        this.reseller = reseller;
        this.products = products;
    }

    // === Getters and Setters ===

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getReseller() {
        return reseller;
    }

    public void setReseller(String reseller) {
        this.reseller = reseller;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }
}