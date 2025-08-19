package uy.edu.ucu.inventario.service;

import uy.edu.ucu.inventario.entity.Deposit;
import uy.edu.ucu.inventario.repository.DepositRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing deposits.
 */
@Service
public class DepositService {

    private final DepositRepository depositRepository;
    private final AuditLogService auditLogService;

    public DepositService(DepositRepository depositRepository, AuditLogService auditLogService) {
        this.depositRepository = depositRepository;
        this.auditLogService = auditLogService;
    }

    public List<Deposit> listAll() {
        return depositRepository.findAll();
    }

    public Optional<Deposit> getById(Long id) {
        return depositRepository.findById(id);
    }

    public Deposit save(Deposit deposit) {
        boolean isNew = (deposit.getId() == null);

        // Validación: nombre obligatorio
        if (deposit.getName() == null || deposit.getName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Deposit name is required.");
        }

        // Validación: no duplicado
        if (isNew && depositRepository.findByNameIgnoreCase(deposit.getName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Deposit with name '" + deposit.getName() + "' already exists.");
        }

        if (isNew) {
            deposit.setProductCount(0); // inicializa contador de productos
            deposit.setAssociatedDate(LocalDateTime.now()); // registra la fecha de creación
        }

        Deposit saved = depositRepository.save(deposit);

        auditLogService.saveLog(
            "Deposit",
            saved.getId(),
            isNew ? "CREATE" : "UPDATE",
            "Deposit name: " + saved.getName()
        );

        return saved;
    }

    public void delete(Long id) {
        Deposit deposit = depositRepository.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Deposit with id " + id + " not found.")
        );

        depositRepository.deleteById(id);

        auditLogService.saveLog(
            "Deposit",
            id,
            "DELETE",
            "Deleted deposit: " + deposit.getName()
        );
    }

    public void incrementProductCount(Deposit deposit) {
        deposit.setProductCount(deposit.getProductCount() + 1);
        depositRepository.save(deposit);
    }

    public void decrementProductCount(Deposit deposit) {
        if (deposit.getProductCount() > 0) {
            deposit.setProductCount(deposit.getProductCount() - 1);
            depositRepository.save(deposit);
        }
    }
}