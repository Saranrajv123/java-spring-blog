package com.example.blogpractice.services;

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
        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "user not found");
        }
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Email already taken!");
        }

        String email = registerDTO.getEmail().toLowerCase();
        String firstName = registerDTO.getFirstName();
        String lastName = registerDTO.getLastName();
        String username = registerDTO.getUsername();
        String password = passwordEncoder.encode(registerDTO.getPassword());
        User newUser = new User(firstName, lastName, email, username, password);

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

        newUser.setRoles(roles);
        User registeredUser = userRepository.save(newUser);
        return modelMapper.map(registeredUser, RegisterDTO.class);
    }

    public LoginResponse login(LoginDTO loginDTO) {
        User user = userRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(() -> new AppException("User not found"));
        System.out.println("username " + user);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
        );

        System.out.println("authenticate " + authentication.getName());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", user);

        return modelMapper.map(response, LoginResponse.class);

    }
}
