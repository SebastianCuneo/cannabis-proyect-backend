package uy.edu.ucu.inventario.controller;

import uy.edu.ucu.inventario.entity.Brand;
import uy.edu.ucu.inventario.service.BrandService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/brands")
public class BrandController {

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> list() {
        List<Brand> brands = brandService.listAll();
        List<Map<String, Object>> brandList = new ArrayList<>();

        for (Brand brand : brands) {
            brandList.add(transformBrand(brand));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", brandList);
        response.put("message", "Brands list retrieved successfully.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> get(@PathVariable Long id) {
        return brandService.getById(id)
                .map(brand -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("data", transformBrand(brand));
                    response.put("message", "Brand found.");
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("error", "Brand not found.");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Brand brand) {
        Brand saved = brandService.save(brand);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", transformBrand(saved));
        response.put("message", "Brand created successfully.");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody Brand brand) {
        return brandService.getById(id)
                .map(existing -> {
                    brand.setId(id);
                    Brand updated = brandService.save(brand);
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("data", transformBrand(updated));
                    response.put("message", "Brand updated successfully.");
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("error", "Brand not found.");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        try {
            brandService.delete(id);
            response.put("success", true);
            response.put("message", "Brand deleted successfully.");
            return ResponseEntity.ok(response);

        } catch (DataIntegrityViolationException ex) {
            response.put("success", false);
            response.put("error", "Cannot delete brand due to data integrity constraints.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);

        } catch (Exception ex) {
            response.put("success", false);
            response.put("error", "Internal error: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private Map<String, Object> transformBrand(Brand brand) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", brand.getId());
        map.put("name", brand.getName());
        map.put("description", brand.getDescription());
        map.put("country", brand.getCountryOfOrigin());
        map.put("createdAt", brand.getCreatedAt());
        map.put("associatedProductCount", brand.getAssociatedProductCount());
        return map;
    }
}