package uy.edu.ucu.inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uy.edu.ucu.inventario.entity.StockMovement;
import uy.edu.ucu.inventario.enums.MovementType;

import java.util.List;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {

    // Buscar movimientos por tipo (ENTRADA, SALIDA, TRANSFERENCIA)
    List<StockMovement> findByType(MovementType type);

    // Buscar por depósito de origen
    List<StockMovement> findByOriginDepositId(Long depositId);

    // Buscar por depósito de destino
    List<StockMovement> findByDestinationDepositId(Long depositId);

    // Buscar transferencias entre dos depósitos específicos
    List<StockMovement> findByOriginDepositIdAndDestinationDepositId(Long originId, Long destinationId);
}