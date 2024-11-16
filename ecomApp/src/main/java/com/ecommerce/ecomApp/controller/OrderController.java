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

import java.util.List;

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
    public Response<Order> updateOrderStatus(@RequestParam("id") String id, @RequestParam("status") String status) {
        log.info("IN ORDER-CONTROLLER :: updateOrderStatus");
        Response<Order> response = orderService.updateOrder(id, status);
        log.info("OUT ORDER-CONTROLLER :: updateOrderStatus");
        return response;
    }

    @GetMapping("/orders")
    public Response<List<Order>> getAllOrdersByUserId(HttpServletRequest request) {
        log.info("IN ORDER-CONTROLLER :: getAllOrdersByUserId");
        Response<List<Order>> response = orderService.findOrdersByUserId(request);
        log.info("OUT ORDER-CONTROLLER :: getAllOrdersByUserId");
        return response;
    }

}
