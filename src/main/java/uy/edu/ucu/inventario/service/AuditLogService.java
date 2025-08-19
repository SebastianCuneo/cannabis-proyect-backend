package uy.edu.ucu.inventario.service;

import org.springframework.stereotype.Service;
import uy.edu.ucu.inventario.entity.AuditLog;
import uy.edu.ucu.inventario.repository.AuditLogRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public AuditLog saveLog(String entityName, Long entityId, String operation, String user) {
        AuditLog log = new AuditLog();
        log.setEntityName(entityName);
        log.setEntityId(entityId);
        log.setOperation(operation);
        log.setUsername(user);
        log.setTimestamp(LocalDateTime.now());
        return auditLogRepository.save(log);
    }

    public List<AuditLog> listAll() {
        return auditLogRepository.findAll();
    }

    public AuditLog save(AuditLog log) {
        return auditLogRepository.save(log);
    }
}
