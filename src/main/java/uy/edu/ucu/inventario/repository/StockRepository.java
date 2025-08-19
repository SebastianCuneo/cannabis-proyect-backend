package uy.edu.ucu.inventario.repository;

import uy.edu.ucu.inventario.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    boolean existsByProductIdAndDepositId(Long productId, Long depositId);

    Optional<Stock> findByProductIdAndDepositId(Long productId, Long depositId);
}