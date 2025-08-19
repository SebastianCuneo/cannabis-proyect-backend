package uy.edu.ucu.security.controllers;

import uy.edu.ucu.security.persistence.entities.UserEntity;
import uy.edu.ucu.security.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class SecurityUserController {

    @Autowired
    IUserService userService;

    @GetMapping("/all")
    private ResponseEntity<List<UserEntity>> getAllUsers(){
        return new ResponseEntity<>(userService.findAllUsers(), HttpStatus.OK);
    }
}
