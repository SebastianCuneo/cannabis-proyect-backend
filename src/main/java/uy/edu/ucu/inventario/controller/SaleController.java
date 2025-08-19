package uy.edu.ucu.inventario.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import uy.edu.ucu.inventario.entity.Product;
import uy.edu.ucu.inventario.entity.Sale;
import uy.edu.ucu.inventario.service.SaleService;

/**
 * REST Controller for the Sale entity.
 */
@RestController
@RequestMapping("/api/sales")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> list() {
        List<Sale> sales = saleService.listAll();
        long totalCount = saleService.getTotalCount();

        List<Map<String, Object>> transformed = new ArrayList<>();
        for (Sale sale : sales) {
            transformed.add(transformSale(sale));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("totalCount", totalCount);
        response.put("data", transformed);
        response.put("message", "Sales list retrieved successfully.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> get(@PathVariable Long id) {
        return saleService.getById(id)
                .map(sale -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("data", transformSale(sale));
                    response.put("message", "Sale found.");
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("error", "Sale not found.");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Sale sale) {
        Sale saved = saleService.save(sale);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", transformSale(saved));
        response.put("message", "Sale created successfully.");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody Sale sale) {
        return saleService.getById(id)
                .map(existing -> {
                    sale.setId(id);
                    Sale updated = saleService.save(sale);
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("data", transformSale(updated));
                    response.put("message", "Sale updated successfully.");
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("error", "Sale not found.");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        try {
            saleService.delete(id);
            response.put("success", true);
            response.put("message", "Sale deleted successfully.");
            return ResponseEntity.ok(response);

        } catch (IllegalStateException ex) {
            response.put("success", false);
            response.put("error", ex.getMessage());
            return ResponseEntity.badRequest().body(response);

        } catch (Exception ex) {
            response.put("success", false);
            response.put("error", "Internal error: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private Map<String, Object> transformSale(Sale sale) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", sale.getId());
        map.put("date", sale.getDate());
        map.put("total", sale.getTotal());
        map.put("paymentMethod", sale.getPaymentMethod());
        map.put("reseller", sale.getReseller());

        List<Map<String, Object>> productList = new ArrayList<>();
        for (Product p : sale.getProducts()) {
            Map<String, Object> pMap = new HashMap<>();
            pMap.put("id", p.getId());
            pMap.put("name", p.getName());
            pMap.put("description", p.getDescription());
            pMap.put("purchasePrice", p.getPurchasePrice());
            pMap.put("salePrice", p.getSalePrice());
            productList.add(pMap);
        }

        map.put("product", productList);
        return map;
    }
}