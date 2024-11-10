package com.ecommerce.ecomApp.controller;

import com.ecommerce.ecomApp.entity.Order;
import com.ecommerce.ecomApp.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody Order order) {
        try {
            return ResponseEntity.ok(orderService.createOrder(order));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllOrders() {
        try{
            return ResponseEntity.ok(orderService.findAllOrders());
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
