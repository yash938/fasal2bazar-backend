package com.agriRoot.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private int userId;
    private String fullName;
    private String password;
    private String email;
    private String adhaarNumber;
    private String panNumber;
    private String address;
    private String image;
    private Set<RoleDto> roles = new HashSet<>();
}
