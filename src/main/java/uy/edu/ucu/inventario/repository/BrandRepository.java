package uy.edu.ucu.inventario.repository;

import uy.edu.ucu.inventario.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    Optional<Brand> findByNameIgnoreCase(String name);
}
