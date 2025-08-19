package uy.edu.ucu.inventario.service;

import uy.edu.ucu.inventario.entity.Product;
import uy.edu.ucu.inventario.entity.Sale;
import uy.edu.ucu.inventario.repository.SaleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing sales.
 */
@Service
public class SaleService {

    private final SaleRepository saleRepository;
    private final AuditLogService auditLogService;

    public SaleService(SaleRepository saleRepository, AuditLogService auditLogService) {
        this.saleRepository = saleRepository;
        this.auditLogService = auditLogService;
    }

    public List<Sale> listAll() {
        return saleRepository.findAll();
    }

    public long getTotalCount() {
        return saleRepository.count();
    }

    public Optional<Sale> getById(Long id) {
        return saleRepository.findById(id);
    }

    public Sale save(Sale sale) {
        boolean isNew = (sale.getId() == null);

        // Validación: debe tener al menos un producto
        List<Product> products = sale.getProducts();
        if (products == null || products.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sale must include at least one product.");
        }

        Sale saved = saleRepository.save(sale);

        auditLogService.saveLog(
            "Sale",
            saved.getId(),
            isNew ? "CREATE" : "UPDATE",
            "Products: " + products.size()
        );

        return saved;
    }

    public void delete(Long id) {
        if (!saleRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sale with id " + id + " not found");
        }

        saleRepository.deleteById(id);

        auditLogService.saveLog(
            "Sale",
            id,
            "DELETE",
            "Sale deleted with id: " + id
        );
    }
}