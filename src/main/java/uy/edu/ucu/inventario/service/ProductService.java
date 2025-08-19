package uy.edu.ucu.inventario.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import uy.edu.ucu.inventario.entity.Brand;
import uy.edu.ucu.inventario.entity.Category;
import uy.edu.ucu.inventario.entity.Deposit;
import uy.edu.ucu.inventario.entity.Product;
import uy.edu.ucu.inventario.repository.ProductRepository;

/**
 * Service for managing products.
 * Contains business logic related to the Product entity.
 */
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final AuditLogService auditLogService;
    private final BrandService brandService;
    private final CategoryService categoryService;
    private final DepositService depositService;

    public ProductService(
        ProductRepository productRepository,
        AuditLogService auditLogService,
        BrandService brandService,
        CategoryService categoryService,
        DepositService depositService
    ) {
        this.productRepository = productRepository;
        this.auditLogService = auditLogService;
        this.brandService = brandService;
        this.categoryService = categoryService;
        this.depositService = depositService;
    }

    public List<Product> listAll() {
        return productRepository.findAll();
    }

    public Optional<Product> getById(Long id) {
        return productRepository.findById(id);
    }

    public Product save(Product product) {
        boolean isNew = (product.getId() == null);

        // Validaciones de datos obligatorios
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product name is required.");
        }

        if (product.getBrand() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product brand is required.");
        }

        if (product.getCategory() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product category is required.");
        }

        // Validación de duplicado
        if (isNew && productRepository.findByNameIgnoreCase(product.getName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Product with name '" + product.getName() + "' already exists.");
        }
     // Lógica para manejar los depósitos al crear el producto
        if (isNew && product.getDeposits() != null && !product.getDeposits().isEmpty()) {

            // Creamos una nueva lista para los depósitos completos
            Set<Deposit> fetchedDeposits = new HashSet<>();

            for (Deposit depositFromRequest : product.getDeposits()) {
                // Buscamos el depósito completo en la base de datos
                depositService.getById(depositFromRequest.getId()).ifPresent(
                    fetchedDeposits::add
                );
            }

            // Asignamos la lista completa de depósitos al producto
            product.setDeposits(fetchedDeposits);
            // Sincronizamos el contador
            product.setDepositsCount(fetchedDeposits.size());
        }
        Product saved = productRepository.save(product);

        if (isNew) {
        	Long idBrand = saved.getBrand().getId();
        	Brand brand = brandService.getById(idBrand).get();
        	Long idCategory = saved.getCategory().getId();
        	Category category = categoryService.getById(idCategory).get();

        	brandService.incrementProductCount(brand);
            categoryService.incrementProductCount(category);
        }

        auditLogService.saveLog(
            "Product",
            saved.getId(),
            isNew ? "CREATE" : "UPDATE",
            "Product name: " + saved.getName()
        );

        return saved;
       }
    

    public void delete(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Product with id " + id + " not found.")
        );

        try {
            productRepository.deleteById(id);

            brandService.decrementProductCount(product.getBrand());
            categoryService.decrementProductCount(product.getCategory());

            auditLogService.saveLog(
                "Product",
                id,
                "DELETE",
                "Product deleted: " + product.getName()
            );
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot delete product due to related records.");
        }
    }

    public void incrementDepositsCount(Product product) {
        product.setDepositsCount(product.getDepositsCount() + 1);
        productRepository.save(product);
    }

    public void decrementDepositsCount(Product product) {
        int current = product.getDepositsCount();
        if (current > 0) {
            product.setDepositsCount(current - 1);
            productRepository.save(product);
        }
    }
}