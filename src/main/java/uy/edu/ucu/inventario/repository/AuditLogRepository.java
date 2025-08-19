package uy.edu.ucu.inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uy.edu.ucu.inventario.entity.AuditLog;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {}