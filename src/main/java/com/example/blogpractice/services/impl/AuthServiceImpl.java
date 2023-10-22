package com.example.blogpractice.services.impl;

import com.example.blogpractice.exceptions.AppException;
import com.example.blogpractice.exceptions.BlogAPIException;
import com.example.blogpractice.modals.Role;
import com.example.blogpractice.modals.User;
import com.example.blogpractice.payloads.LoginDTO;
import com.example.blogpractice.payloads.RegisterDTO;
import com.example.blogpractice.payloads.response.LoginResponse;
import com.example.blogpractice.respository.RoleRepository;
import com.example.blogpractice.respository.UserRepository;
import com.example.blogpractice.security.JWTProvider;
import com.example.blogpractice.utils.AppConstants;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.*;

@Service
public class AuthServiceImpl {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTProvider jwtProvider;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    public RegisterDTO registerUser(RegisterDTO registerDTO) {
        System.out.println("registerDTO" + registerDTO);
        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            throw new AppException("Username already found");
        }
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new AppException("Email already taken!");
        }

//        String email = registerDTO.getEmail().toLowerCase();
//        String firstName = registerDTO.getFirstName();
//        String lastName = registerDTO.getLastName();
//        String username = registerDTO.getUsername();
//        String password = passwordEncoder.encode(registerDTO.getPassword());
//        User newUser = new User(firstName, lastName, email, username, password);
        registerDTO.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

        User user = modelMapper.map(registerDTO, User.class);

        Set<Role> roles = new HashSet<>();
        if (userRepository.count() == 0) {
            roles.add(
            roleRepository.findByName("ROLE_ADMIN").orElseThrow(() -> new AppException(AppConstants.USER_ROLE_NOT_SET))
            );
            roles.add(
                    roleRepository.findByName("ROLE_USER").orElseThrow(() -> new AppException(AppConstants.USER_ROLE_NOT_SET))
            );
        } else {
            roles.add(roleRepository.findByName("ROLE_ADMIN")
                    .orElseThrow(() -> new AppException(AppConstants.USER_ROLE_NOT_SET)));
        }

        user.setRoles(roles);
        User registeredUser = userRepository.save(user);
        return modelMapper.map(registeredUser, RegisterDTO.class);
    }

    public LoginResponse login(LoginDTO loginDTO) {
//        System.out.println("login " + loginDTO.getEmail());
        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new AppException("User not found"));
        System.out.println("user from login " + user.getEmail());
        System.out.println("user from login " + user.getPassword());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
            );

            System.out.println("authenticate get credentials" + authentication);

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtProvider.generateToken(user.getEmail());
            System.out.println("token " + token);
            LoginResponse res = modelMapper.map(user, LoginResponse.class);
            res.setToken(token);

            System.out.println("res " + res);

            return modelMapper.map(res, LoginResponse.class);

        } catch (Exception e) {
            throw new AppException("exception  " + e.getMessage());
        }


    }
}
