package uy.edu.ucu.inventario.repository;

import uy.edu.ucu.inventario.entity.Provider;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {
	Optional<Provider> findByNameIgnoreCase(String name);
}