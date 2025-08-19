package uy.edu.ucu.inventario.service;

import uy.edu.ucu.inventario.entity.Category;
import uy.edu.ucu.inventario.repository.CategoryRepository;
import uy.edu.ucu.inventario.repository.ProductRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final AuditLogService auditLogService;

    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository, AuditLogService auditLogService) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.auditLogService = auditLogService;
    }

    public List<Category> listAll() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getById(Long id) {
        return categoryRepository.findById(id);
    }

    public Category save(Category category) {
        boolean isNew = (category.getId() == null);

        // Validar campos obligatorios
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category name is required.");
        }

        if (isNew) {
            // Validar que no exista otra categoría con el mismo nombre
            if (categoryRepository.findByNameIgnoreCase(category.getName()).isPresent()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Category with that name already exists.");
            }

            category.setAssociatedProductCount(0); // Inicializamos contador si es nueva
        }

        Category saved = categoryRepository.save(category);

        auditLogService.saveLog(
            "Category",
            saved.getId(),
            isNew ? "CREATE" : "UPDATE",
            null // Username más adelante desde Spring Security
        );

        return saved;
    }

    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with id " + id + " not found.");
        }

        if (productRepository.existsByCategoryId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot delete category because it is used by products.");
        }

        categoryRepository.deleteById(id);

        auditLogService.saveLog(
            "Category",
            id,
            "DELETE",
            null
        );
    }

    public void incrementProductCount(Category category) {
        category.incrementAssociatedProductCount();
        categoryRepository.save(category);
    }

    public void decrementProductCount(Category category) {
        category.decrementAssociatedProductCount();
        categoryRepository.save(category);
    }
}