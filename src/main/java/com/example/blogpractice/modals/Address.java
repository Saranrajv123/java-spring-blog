package com.example.blogpractice.modals;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "address")
public class Address extends UserDateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "VARCHAR(255)", nullable = false, updatable = false)
    private String id;
    @NotBlank
    @Size(min = 5, message = "Street name must contain at least 5 characters")
    private String street;
    @NotBlank
    @Size(min = 5, message = "build name name must contain at least 5 characters")
    private String buildName;

    @NotBlank
    @Size(min = 5, message = "City name must contain at least 5 characters")
    private String city;
    @NotBlank
    @Size(min = 5, message = "State name must contain at least 5 characters")
    private String state;
    @NotBlank
    @Size(min = 5, message = "zipcode name must contain at least 5 characters")
    private String zipcode;
    @NotBlank
    @Size(min = 2, message = "country name must contain at least 5 characters")
    private String country;

    @JsonIgnore
    @OneToOne(mappedBy = "address")
    private User user;



}
