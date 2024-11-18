package com.ecommerce.ecomApp.controller;

import com.ecommerce.ecomApp.entity.User;
import com.ecommerce.ecomApp.response.Response;
import com.ecommerce.ecomApp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public Response<List<User>> getAllUsers() {
      log.info("IN USER-CONTROLLER :: getAllUsers");
      Response<List<User>> response =  userService.getAllUsers();
      log.info("OUT USER-CONTROLLER :: getAllUsers");
      return response;
    }
}
