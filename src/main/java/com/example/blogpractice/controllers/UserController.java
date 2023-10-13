package com.example.blogpractice.controllers;

import com.example.blogpractice.payloads.CreateUserRequestDTO;
import com.example.blogpractice.services.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blog/users")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/create-user")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CreateUserRequestDTO> createUserController(@RequestBody @Valid CreateUserRequestDTO createUserRequestDTO) {
        return new ResponseEntity<>(
                userService.createUser(createUserRequestDTO),
                HttpStatus.CREATED
        );
    }


    @PutMapping("/update-user/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable String id,
            @RequestBody @Valid CreateUserRequestDTO createUserRequestDTO

    ) {
        userService.updateUser(id, createUserRequestDTO);

        return null;


    }
}
