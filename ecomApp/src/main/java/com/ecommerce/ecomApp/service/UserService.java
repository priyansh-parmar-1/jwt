package com.ecommerce.ecomApp.service;

import com.ecommerce.ecomApp.dto.UserDto;
import com.ecommerce.ecomApp.entity.Role;
import com.ecommerce.ecomApp.entity.User;
import com.ecommerce.ecomApp.repository.UserRepository;
import com.ecommerce.ecomApp.response.Response;
import com.ecommerce.ecomApp.response.ResponseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Response<UserDto> registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return Response.<UserDto>builder()
                    .status(ResponseConstants.ERROR)
                    .error("Username is already in use")
                    .build();
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(Set.of(Role.USER));
        }
        try {
            userRepository.save(user);
            UserDto userDto = new UserDto(user);
            return Response.<UserDto>builder()
                    .status(ResponseConstants.SUCCESS)
                    .data(userDto)
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.<UserDto>builder()
                    .status(ResponseConstants.ERROR)
                    .error(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.<UserDto>builder()
                    .status(ResponseConstants.ERROR)
                    .error(e.getMessage())
                    .build();
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> usernameOptional = userRepository.findByUsername(username);
        if (usernameOptional.isPresent()) {
            User user = usernameOptional.get();

            List<GrantedAuthority> authorities = user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                    .collect(Collectors.toList());

            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    authorities);
        } else {
            throw new UsernameNotFoundException("Username not found");
        }
    }

    public Response<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        if (!users.isEmpty()) {
            return Response.<List<User>>builder()
                    .status(ResponseConstants.SUCCESS)
                    .data(users)
                    .build();
        } else {
            return Response.<List<User>>builder()
                    .status(ResponseConstants.ERROR)
                    .error("No users found")
                    .build();
        }
    }

}
