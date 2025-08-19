package uy.edu.ucu.inventario.repository;

import uy.edu.ucu.inventario.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Permite buscar un usuario por su email (útil para login o validaciones futuras)
    boolean existsByEmail(String email);
    boolean existsByEmailIgnoreCase(String email);
}