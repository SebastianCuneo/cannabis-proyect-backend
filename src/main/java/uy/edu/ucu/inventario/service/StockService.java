package uy.edu.ucu.inventario.service;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import uy.edu.ucu.inventario.entity.Deposit;
import uy.edu.ucu.inventario.entity.Product;
import uy.edu.ucu.inventario.entity.Stock;
import uy.edu.ucu.inventario.repository.StockRepository;

/**
 * Service for managing product stock in deposits.
 */
@Service
public class StockService {

    private final StockRepository stockRepository;
    private final AuditLogService auditLogService;
    private final ProductService productService;
    private final DepositService depositService;

    public StockService(
        StockRepository stockRepository,
        AuditLogService auditLogService,
        ProductService productService,
        DepositService depositService
    ) {
        this.stockRepository = stockRepository;
        this.auditLogService = auditLogService;
        this.productService = productService;
        this.depositService = depositService;
    }

    public List<Stock> listAll() {
        return stockRepository.findAll();
    }

    public Optional<Stock> getById(Long id) {
        return stockRepository.findById(id);
    }

    public Stock save(Stock stock) {
        boolean isNew = (stock.getId() == null);

        // Validaciones
        if (stock.getProduct() == null || stock.getProduct().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stock must be associated with a product.");
        }

        if (stock.getDeposit() == null || stock.getDeposit().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stock must be associated with a deposit.");
        }

        if (stock.getQuantity() == null || stock.getQuantity() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stock quantity must be specified and not negative.");
        }

        // --- Cargar producto y deposito completos para evitar errores de Hibernate ---
        Product fullProduct = productService.getById(stock.getProduct().getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product not found"));
        stock.setProduct(fullProduct);

        Deposit fullDeposit = depositService.getById(stock.getDeposit().getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Deposit not found"));
        stock.setDeposit(fullDeposit);
        
        if (isNew) {
            boolean existsInDeposit = stockRepository.existsByProductIdAndDepositId(
                stock.getProduct().getId(), stock.getDeposit().getId()
            );

            if (existsInDeposit) {
                throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This product already has stock in the selected deposit."
                );
            }

            productService.incrementDepositsCount(stock.getProduct());
            depositService.incrementProductCount(stock.getDeposit());
        }

        Stock saved = stockRepository.save(stock);

        auditLogService.saveLog(
            "Stock",
            saved.getId(),
            isNew ? "CREATE" : "UPDATE",
            null
        );

        return saved;
    }

    public void delete(Long id) {
        Optional<Stock> stockOpt = stockRepository.findById(id);

        if (stockOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Stock with id " + id + " not found");
        }

        Stock stock = stockOpt.get();

        try {
            stockRepository.deleteById(id);

            productService.decrementDepositsCount(stock.getProduct());
            depositService.decrementProductCount(stock.getDeposit());

            auditLogService.saveLog(
                "Stock",
                id,
                "DELETE",
                null
            );

        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Cannot delete stock because it is referenced by other records",
                ex
            );
        }
    }
}