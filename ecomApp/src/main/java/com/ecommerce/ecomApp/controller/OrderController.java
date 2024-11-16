package com.ecommerce.ecomApp.controller;

import com.ecommerce.ecomApp.entity.Order;
import com.ecommerce.ecomApp.response.Response;
import com.ecommerce.ecomApp.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public Response<Order> createOrder(@RequestBody Order order, HttpServletRequest request) {
        log.info("IN ORDER-CONTROLLER :: createOrder :: order :: {},request :: {}", order,request);
        Response<Order> response = orderService.createOrder(order, request);
        log.info("OUT ORDER-CONTROLLER :: createOrder");
        return response;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllOrders() {
        try {
            return ResponseEntity.ok(orderService.findAllOrders());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping
    public ResponseEntity<?> updateOrderStatus(@RequestParam("id") String id, @RequestParam("status") String status) {
        try {
            return ResponseEntity.ok(orderService.updateOrder(id, status));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getAllOrdersByUserId(HttpServletRequest request) {
        try {
            return ResponseEntity.ok(orderService.findOrdersByUserId(request));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
