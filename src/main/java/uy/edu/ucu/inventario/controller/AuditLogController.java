package uy.edu.ucu.inventario.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uy.edu.ucu.inventario.entity.AuditLog;
import uy.edu.ucu.inventario.service.AuditLogService;

@RestController
@RequestMapping("/api/audit-log")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping
    public ResponseEntity<List<AuditLog>> getAll() {
        return ResponseEntity.ok(auditLogService.listAll());
    }

    @PostMapping
    public ResponseEntity<AuditLog> create(@RequestBody AuditLog log) {
        AuditLog saved = auditLogService.save(log);
        return ResponseEntity.status(201).body(saved); // 201 CREATED
    }
}
