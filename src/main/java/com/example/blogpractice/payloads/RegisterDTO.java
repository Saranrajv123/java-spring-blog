package com.example.blogpractice.payloads;

import com.example.blogpractice.modals.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {
    private String id;
    @Size(min = 6, max = 30, message = "username must be at least 6 characters")
    @NotBlank
    @NotEmpty
    private String username;
    @Size(min = 6, max = 30, message = "first name must be at least 6 characters")
    @NotBlank
    @NotEmpty
    private String firstName;
    @Size(min = 6, max = 30, message = "lastname must be at least 6 characters")
    @NotBlank
    @NotEmpty
    private String lastName;
    @NotBlank
    @NotEmpty
    @Email(message = "Not valid email")
    private String email;
    @Size(min = 6, max = 32)
    @NotBlank
    @NotEmpty
    private String password;

    @Size(min = 6, max = 12)
    @NotBlank
    @NotEmpty
    private String phone;

    private Set<Role> roles = new HashSet<>();

}
