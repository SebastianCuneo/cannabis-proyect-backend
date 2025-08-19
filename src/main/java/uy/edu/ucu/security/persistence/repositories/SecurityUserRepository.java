package uy.edu.ucu.security.persistence.repositories;

import uy.edu.ucu.security.persistence.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SecurityUserRepository extends JpaRepository<UserEntity, Long> {

    @Query(value = "SELECT * FROM users WHERE email = :email", nativeQuery = true)
    Optional<UserEntity> findByEmail(String email);
}
