package com.example.blogpractice.services.impl;

import com.example.blogpractice.exceptions.AppException;
import com.example.blogpractice.modals.Confirmation;
import com.example.blogpractice.modals.Role;
import com.example.blogpractice.modals.User;
import com.example.blogpractice.payloads.CreateUserRequestDTO;
import com.example.blogpractice.payloads.response.CreateUserResponseDTO;
import com.example.blogpractice.respository.ConfirmationRepository;
import com.example.blogpractice.respository.RoleRepository;
import com.example.blogpractice.respository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl {
    
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ConfirmationRepository confirmationRepository;

    public CreateUserRequestDTO createUser(CreateUserRequestDTO createUserRequestDTO) {
        System.out.println("createUser " + createUserRequestDTO);
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
        user.setIsEnabled(false);
        User savedUser = userRepository.save(user);
        Confirmation confirmationUser = new Confirmation();
        confirmationUser.setUser(savedUser);
        Confirmation confirmationSavedUser = confirmationRepository.save(confirmationUser);
        return modelMapper.map(savedUser, CreateUserRequestDTO.class);
    }

    public CreateUserRequestDTO updateUser(String id, CreateUserRequestDTO createUserRequestDTO) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new AppException("User not found")
        );
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("suth " + auth.getDetails());
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) || auth.getName().equals(user.getUsername())) {
//            User user1 = modelMapper.map(createUserRequestDTO, User.class);
            user.setAddress(createUserRequestDTO.getAddress());
            user.setUsername(createUserRequestDTO.getUsername());
            user.setEmail(createUserRequestDTO.getUsername());
            user.setFirstName(createUserRequestDTO.getFirstName());
            user.setLastName(createUserRequestDTO.getLastName());
            user.setPhone(createUserRequestDTO.getPhone());
            user.setPassword(passwordEncoder.encode(createUserRequestDTO.getPassword()));
            User updatedUser = userRepository.save(user);
            return modelMapper.map(updatedUser, CreateUserRequestDTO.class);
        }

        throw new AppException("Something went wrong!");
    }

    public Boolean verifyToken(String token) {
        Confirmation confirmation = confirmationRepository.findByToken(token);
        User user = userRepository.findByEmail(confirmation.getUser().getEmail()).orElseThrow(
                () -> new RuntimeException("User not found")
        );

        user.setIsEnabled(true);
        userRepository.save(user);
        return Boolean.TRUE;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public String deleteUser(String id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new AppException("User not found")
        );

        System.out.println("user " + user);



        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        System.out.println(authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
        if (
                !authentication.getName().equals(user.getEmail()) ||
                !authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))
        ) {
            throw new AppException("Access denied");
        }
        userRepository.deleteById(id);
        return "user deleted successfully " + " " + id;
    }

    public String giveAdminAccessToUser(String id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new AppException("User not found")
        );

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))
                || !authentication.getName().equals(user.getUsername())
        ) {
            throw new AppException("Access denied");
        }

        Set<Role> roles = new HashSet<>();
        roles.add(
        roleRepository.findByName("ROLE_ADMIN").orElseThrow(
                () -> new AppException("Role not found!")
        ));
        roles.add(
        roleRepository.findByName("ROLE_USER").orElseThrow(
                () -> new AppException("Role not found!")
        ));
        user.setRoles(roles);
        User user1 = userRepository.save(user);
        return "You gave admin access to user " + user1.getId();
    }

    public String revertAdminAccessToUser(String id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new AppException("User not found")
        );

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))
                || !authentication.getName().equals(user.getUsername())
        ) {
            throw new AppException("Access denied");
        }

        Set<Role> roles = new HashSet<>();
        roles.add(
                roleRepository.findByName("ROLE_USER").orElseThrow(
                        () -> new AppException("Role not found!")
                ));
        user.setRoles(roles);
        User user1 = userRepository.save(user);
        return "You revert admin access to user " + user1.getId();
    }
}
