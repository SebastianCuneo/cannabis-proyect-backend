package uy.edu.ucu.inventario.controller;

import uy.edu.ucu.inventario.entity.User;
import uy.edu.ucu.inventario.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> list() {
        List<User> users = userService.listAll();
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", users);
        response.put("message", "User list retrieved successfully.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> get(@PathVariable Long id) {
        return userService.getById(id)
                .map(user -> {
                    Map<String, Object> response = new HashMap<>();
                    Map<String, Object> userData = new HashMap<>();
                    userData.put("id", user.getId());
                    userData.put("firstName", user.getFirstName());
                    userData.put("lastName", user.getLastName());
                    userData.put("email", user.getEmail());
                    userData.put("phone", user.getPhone());

                    response.put("success", true);
                    response.put("data", userData);
                    response.put("message", "User found.");
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> error = new HashMap<>();
                    error.put("success", false);
                    error.put("error", "User not found.");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                });
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> create(@RequestBody User user) {
        User saved = userService.save(user);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", saved);
        response.put("message", "User created successfully.");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody User user) {
        return userService.getById(id)
                .map(existing -> {
                    user.setId(id);
                    User updated = userService.save(user);
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("data", updated);
                    response.put("message", "User updated successfully.");
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> error = new HashMap<>();
                    error.put("success", false);
                    error.put("error", "User not found.");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        userService.delete(id);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "User deleted successfully.");
        return ResponseEntity.ok(response);
    }
}