	package uy.edu.ucu.inventario.service;

import uy.edu.ucu.inventario.entity.User;
import uy.edu.ucu.inventario.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing users.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuditLogService auditLogService;

    public UserService(UserRepository userRepository, AuditLogService auditLogService) {
        this.userRepository = userRepository;
        this.auditLogService = auditLogService;
    }

    public List<User> listAll() {
        return userRepository.findAll();
    }

    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }

    public User save(User user) {
        boolean isNew = (user.getId() == null);

        // Validaciones obligatorias
        if (user.getFirstName() == null || user.getFirstName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "First name is required.");
        }
        if (user.getLastName() == null || user.getLastName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Last name is required.");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required.");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is required.");
        }

        if (isNew && userRepository.existsByEmailIgnoreCase(user.getEmail())) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "A user with that email already exists."
            );
        }

        User saved = userRepository.save(user);

        auditLogService.saveLog(
            "User",
            saved.getId(),
            isNew ? "CREATE" : "UPDATE",
            null
        );

        return saved;
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + id + " not found");
        }

        userRepository.deleteById(id);

        auditLogService.saveLog(
            "User",
            id,
            "DELETE",
            null
        );
    }
}