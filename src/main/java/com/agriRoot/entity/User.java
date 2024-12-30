package com.agriRoot.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @NotNull(message = "email must be required")
    private String fullName;

    @NotNull(message = "Password is required")
    private String password;
    @Column(unique = true)
    @NotNull(message = "email must be required")
    private String email;

    @NotNull(message = "Adhaar number must be required")
    private String adhaarNumber;

    @NotNull(message = "PanCard must be required")
    private String panNumber;

    @NotNull(message = "Address is required")
    private String address;
    private String image;
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles = new ArrayList<>();

}
