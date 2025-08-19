package uy.edu.ucu.inventario.controller;

import uy.edu.ucu.inventario.entity.Provider;
import uy.edu.ucu.inventario.service.ProviderService;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

/**
 * REST Controller for the Provider entity.
 */
@RestController
@RequestMapping("/api/providers")
public class ProviderController {

    private final ProviderService providerService;

    public ProviderController(ProviderService providerService) {
        this.providerService = providerService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> list() {
        List<Provider> providers = providerService.listAll();
        List<Map<String, Object>> transformed = new ArrayList<>();

        for (Provider provider : providers) {
            transformed.add(transformProvider(provider));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", transformed);
        response.put("message", "Providers list retrieved successfully.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> get(@PathVariable Long id) {
        return providerService.getById(id)
                .map(provider -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("data", transformProvider(provider));
                    response.put("message", "Provider found.");
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("error", "Provider not found.");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Provider provider) {
        Provider saved = providerService.save(provider);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", transformProvider(saved));
        response.put("message", "Provider created successfully.");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody Provider provider) {
        return providerService.getById(id)
                .map(existing -> {
                    provider.setId(id);
                 // Copia los valores del nuevo objeto al existente
                    existing.setName(provider.getName());
                    existing.setEmail(provider.getEmail());
                    existing.setPhone(provider.getPhone());
                    existing.setAddress(provider.getAddress());
                    
                    Provider updated = providerService.save(provider);
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("data", transformProvider(updated));
                    response.put("message", "Provider updated successfully.");
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("error", "Provider not found.");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        try {
            providerService.delete(id);
            response.put("success", true);
            response.put("message", "Provider deleted successfully.");
            return ResponseEntity.ok(response);

        } catch (DataIntegrityViolationException ex) {
            response.put("success", false);
            response.put("error", "Cannot delete provider due to data integrity constraints.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);

        } catch (Exception ex) {
            response.put("success", false);
            response.put("error", "Internal error: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private Map<String, Object> transformProvider(Provider provider) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", provider.getId());
        map.put("name", provider.getName());
        map.put("email", provider.getEmail());
        map.put("phone", provider.getPhone());
        map.put("address", provider.getAddress());
        map.put("associatedDate", provider.getDate());
        return map;
    }
}