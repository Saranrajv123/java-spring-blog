package com.example.blogpractice.controllers;

import com.example.blogpractice.modals.User;
import com.example.blogpractice.payloads.CreateUserRequestDTO;
import com.example.blogpractice.services.UserServiceImpl;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blog")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/admin/create-user")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CreateUserRequestDTO> createUserController(@RequestBody @Valid CreateUserRequestDTO createUserRequestDTO) {
        return new ResponseEntity<>(
                userService.createUser(createUserRequestDTO),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/admin/users")
    public ResponseEntity<List<User>> getUsersController() {
        return new ResponseEntity<>(
                userService.getUsers(),
                HttpStatus.OK

        );
    }


    @PutMapping("/public/update-user/{id}")
    public ResponseEntity<CreateUserRequestDTO> updateUser(
            @PathVariable String id,
            @RequestBody @Valid CreateUserRequestDTO createUserRequestDTO

    ) {
        return new ResponseEntity<>(
                userService.updateUser(id, createUserRequestDTO),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/public/delete-user/{id}")
    public ResponseEntity<?> deleteUserController(
            @PathVariable String id
    ) {
        return new ResponseEntity<>(
                userService.deleteUser(id),
                HttpStatus.OK
        );
    }

    @PostMapping("/admin/grant-admin-permission/{id}")
    public ResponseEntity<String> giveAdminAccessToUserController(@PathVariable String id) {
        return new ResponseEntity<>(
                userService.giveAdminAccessToUser(id),
                HttpStatus.OK
        );
    }

    @PostMapping("/admin/revert-admin-permission/{id}")
    public ResponseEntity<String> revertAdminAccessToUserController(@PathVariable String id) {
        return new ResponseEntity<>(
                userService.revertAdminAccessToUser(id),
                HttpStatus.OK
        );
    }





}
