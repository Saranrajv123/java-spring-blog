package com.example.blogpractice.modals;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "users",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"username"}),
                @UniqueConstraint(columnNames = {"email"})}
)
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    public User(String firstName, String lastName, String email, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public List<Comment> getComments() {
        return comments == null ? null : new ArrayList<>();
    }
    public void setComments(List<Comment> comments) {
        if (comments == null) {
            this.comments = null;
        } else {
            this.comments = Collections.unmodifiableList(comments);
        }

    }
}
