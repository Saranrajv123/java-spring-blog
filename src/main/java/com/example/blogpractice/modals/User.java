package com.example.blogpractice.modals;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "VARCHAR(255)", nullable = false, updatable = false)
    private String id;

    @NotBlank
    @Column(name = "first_name", columnDefinition = "VARCHAR(255)")
    @Size(max = 40)
    private String firstName;

    @NotBlank
    @Column(name = "last_name", columnDefinition = "VARCHAR(255)")
    @Size(max = 40)
    private String lastName;

    @NotBlank
    @Column(name = "username", columnDefinition = "VARCHAR(255)")
    @Size(max = 30)
    private String username;

    @NotBlank
    @Size(max = 100)
    @Column(name = "password", columnDefinition = "VARCHAR(255)")
    private String password;

    @NotBlank
    @Size(max = 40)
    @Column(name = "email", columnDefinition = "VARCHAR(255)")
    @Email
    private String email;

    @Column(name = "phone")
    private String phone;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_id")
    private Address address;

    public User(String firstName, String lastName, String email, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
    }
}
