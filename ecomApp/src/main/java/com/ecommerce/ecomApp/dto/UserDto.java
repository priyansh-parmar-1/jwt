package com.ecommerce.ecomApp.dto;

import com.ecommerce.ecomApp.entity.Role;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDto {

    private String username;
    private String password;
    private Set<Role> roles;
}
