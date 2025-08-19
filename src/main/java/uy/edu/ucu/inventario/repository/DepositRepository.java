package uy.edu.ucu.inventario.repository;

import uy.edu.ucu.inventario.entity.Deposit;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepositRepository extends JpaRepository<Deposit, Long> {
	Optional<Deposit> findByNameIgnoreCase(String name);
}