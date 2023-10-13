package com.example.blogpractice.services;

import com.example.blogpractice.exceptions.AppException;
import com.example.blogpractice.modals.Role;
import com.example.blogpractice.modals.User;
import com.example.blogpractice.payloads.CreateUserRequestDTO;
import com.example.blogpractice.payloads.response.CreateUserResponseDTO;
import com.example.blogpractice.respository.RoleRepository;
import com.example.blogpractice.respository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    public CreateUserRequestDTO createUser(CreateUserRequestDTO createUserRequestDTO) {
        if (userRepository.existsByUsername(createUserRequestDTO.getUsername())) {
            throw new AppException("Username is already taken");
        }

        if (userRepository.existsByEmail(createUserRequestDTO.getEmail())) {
            throw new AppException("Email already taken");
        }

        User user = modelMapper.map(createUserRequestDTO, User.class);

        Set<Role> roles = new HashSet<>();
        roles.add(
                roleRepository.findByName("ROLE_USER").orElseThrow(
                        () -> new AppException("Role not found")
                )
        );
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(createUserRequestDTO.getPassword()));
        User savedUser = userRepository.save(user);
        CreateUserRequestDTO createUserRequestDTO1 = modelMapper.map(user, CreateUserRequestDTO.class);
        return createUserRequestDTO;
    }

    public CreateUserRequestDTO updateUser(String id, CreateUserRequestDTO createUserRequestDTO) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new AppException("User not found")
        );
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("suth " + auth.getDetails());
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) || auth.getName().equals(user.getUsername())) {

        }

        return null;
    }
}
