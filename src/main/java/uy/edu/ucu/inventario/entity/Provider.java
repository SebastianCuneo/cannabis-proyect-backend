package uy.edu.ucu.inventario.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity representing a provider (supplier).
 */
@Entity
@Table(name = "providers")
public class Provider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 100)
    private String email;

    @Column(length = 50)
    private String phone;

    @Column(length = 255)
    private String address;

    @Column(nullable = false)
    private LocalDate associated_date;
    
    // Método que se ejecuta justo antes de guardar la entidad por primera vez
    @PrePersist
    protected void onCreate() {
        this.associated_date = LocalDate.now();
    }
    
    // Método que se ejecuta justo antes de actualizar la entidad
    @PreUpdate
    protected void onUpdate() {
        this.associated_date = LocalDate.now();
    }
    

    // === Constructors ===

    public Provider() {}

    public Provider(String name, String email, String phone, String address) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.associated_date = LocalDate.now();
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public void setDate(LocalDate date) {
        this.associated_date = date;
    }
    
 // Código corregido (getter correcto):
    public LocalDate getDate() {
        return this.associated_date;
    }
}