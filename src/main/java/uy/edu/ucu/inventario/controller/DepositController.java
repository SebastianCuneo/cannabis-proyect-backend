package uy.edu.ucu.inventario.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uy.edu.ucu.inventario.entity.Deposit;
import uy.edu.ucu.inventario.service.DepositService;

/**
 * REST Controller for managing deposits.
 */
@RestController
@RequestMapping("/api/deposits")
public class DepositController {

    private final DepositService depositService;

    public DepositController(DepositService depositService) {
        this.depositService = depositService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> list() {
        List<Deposit> deposits = depositService.listAll();
        List<Map<String, Object>> transformed = new ArrayList<>();

        for (Deposit deposit : deposits) {
            transformed.add(transformDeposit(deposit));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", transformed);
        response.put("message", "Deposit list retrieved successfully.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> get(@PathVariable Long id) {
        return depositService.getById(id)
                .map(deposit -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("data", transformDeposit(deposit));
                    response.put("message", "Deposit found.");
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("error", "Deposit not found.");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Deposit deposit) {
        Deposit saved = depositService.save(deposit);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", transformDeposit(saved));
        response.put("message", "Deposit created successfully.");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody Deposit deposit) {
        return depositService.getById(id)
                .map(existing -> {
                    deposit.setId(id);
                    Deposit updated = depositService.save(deposit);
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("data", transformDeposit(updated));
                    response.put("message", "Deposit updated successfully.");
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("error", "Deposit not found.");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        try {
            depositService.delete(id);
            response.put("success", true);
            response.put("message", "Deposit deleted successfully.");
            return ResponseEntity.ok(response);

        } catch (DataIntegrityViolationException ex) {
            response.put("success", false);
            response.put("error", "Cannot delete deposit due to data integrity constraints.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);

        } catch (Exception ex) {
            response.put("success", false);
            response.put("error", "Internal error: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private Map<String, Object> transformDeposit(Deposit deposit) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", deposit.getId());
        map.put("name", deposit.getName());
        map.put("location", deposit.getLocation());
        map.put("description", deposit.getDescription());
        map.put("productCount", deposit.getProductCount());
        map.put("associatedDate", deposit.getAssociatedDate());
        return map;
    }
}