package com.example.blogpractice.controllers;

import com.example.blogpractice.payloads.LoginDTO;
import com.example.blogpractice.payloads.RegisterDTO;
import com.example.blogpractice.payloads.response.LoginResponse;
import com.example.blogpractice.services.impl.AuthServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/blog")
public class AuthController {

    @Autowired
    private AuthServiceImpl authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterDTO> registerController(@RequestBody @Valid RegisterDTO registerDTO) {
        return new ResponseEntity<>(
                authService.registerUser(registerDTO),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginController(@RequestBody @Valid LoginDTO loginDTO) {
        return new ResponseEntity<>(
                authService.login(loginDTO),
                HttpStatus.OK
        );
    }
}
