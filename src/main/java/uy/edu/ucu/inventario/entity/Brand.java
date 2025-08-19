package uy.edu.ucu.inventario.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing a brand.
 * A brand can be associated with multiple products.
 */
@Entity
@Table(name = "brands")
public class Brand {

    // === Attributes ===

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(length = 100)
    private String countryOfOrigin;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private int associatedProductCount = 0;

    // === Constructors ===

    public Brand() {}

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // === Getters and Setters ===

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

    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public void setCountryOfOrigin(String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public int getAssociatedProductCount() {
        return associatedProductCount;
    }

    public void setAssociatedProductCount(int associatedProductCount) {
        this.associatedProductCount = associatedProductCount;
    }

    public void incrementAssociatedProductCount() {
        this.associatedProductCount++;
    }

    public void decrementAssociatedProductCount() {
        if (this.associatedProductCount > 0) {
            this.associatedProductCount--;
        }
    }
    
    @Override
    public String toString() {
        return "Brand{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", description='" + description + '\'' +
               ", countryOfOrigin='" + countryOfOrigin + '\'' +
               ", createdAt=" + createdAt +
               ", associatedProductCount=" + associatedProductCount +
               '}';
    }

}