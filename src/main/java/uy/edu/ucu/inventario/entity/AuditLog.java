package uy.edu.ucu.inventario.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String operation; // create, update, delete

    @Column(nullable = false, length = 100)
    private String entityName;

    @Column(nullable = false)
    private Long entityId;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(length = 100)
    private String username;

    @Column(length = 500)
    private String details;

    // === Constructores ===

    public AuditLog() {
    }

    public AuditLog(String operation, String entityName, Long entityId, LocalDateTime timestamp, String username, String details) {
        this.operation = operation;
        this.entityName = entityName;
        this.entityId = entityId;
        this.timestamp = timestamp;
        this.username = username;
        this.details = details;
    }

    // === Getters y setters ===

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}