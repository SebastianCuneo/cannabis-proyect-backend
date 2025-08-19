package uy.edu.ucu.inventario.repository;

import uy.edu.ucu.inventario.entity.Product;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Verifica si hay productos con una marca específica
    boolean existsByBrandId(Long brandId);

    // Verifica si hay productos con una categoría específica
    boolean existsByCategoryId(Long categoryId);
    
    Optional<Product> findByNameIgnoreCase(String name);
}