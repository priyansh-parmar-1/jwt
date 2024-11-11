package com.ecommerce.ecomApp.service.impl;

import com.ecommerce.ecomApp.entity.*;
import com.ecommerce.ecomApp.jwt.JwtUtils;
import com.ecommerce.ecomApp.repository.OrderRepository;
import com.ecommerce.ecomApp.repository.ProductRepository;
import com.ecommerce.ecomApp.repository.UserRepository;
import com.ecommerce.ecomApp.service.OrderService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public Order createOrder(Order order, HttpServletRequest request) {
        if (order != null) {
            double totalPrice = 0.0;
            for (OrderItem orderItem : order.getOrderItems()) {
                Optional<Product> productOpt = productRepository.findById(orderItem.getProductId());

                if (productOpt.isEmpty()) {
                    throw new RuntimeException("Product not found: " + orderItem.getProductId());
                }

                Product product = productOpt.get();
                if (product.getStock() < orderItem.getQuantity()) {
                    throw new RuntimeException("Insufficient stock for product: " + product.getName());
                }

                totalPrice += orderItem.getQuantity() * product.getPrice();

                product.setStock(product.getStock() - orderItem.getQuantity());
                productRepository.save(product);
            }
            String id = getUserIdFromJwtCookie(request);
            order.setUserId(id);
            order.setTotalPrice(totalPrice);
            order.setStatus(OrderStatus.CONFIRM);
            order.setOrderDate(String.valueOf(new Date()));
            return orderRepository.save(order);
        } else {
            throw new RuntimeException("Order is null");
        }
    }


    @Override
    public List<Order> findAllOrders() {

        List<Order> allOrders = orderRepository.findAll();
        if (allOrders.isEmpty())
            throw new RuntimeException("No products found");
        else
            return allOrders;
    }

    @Override
    public List<Order> findOrdersByUserId(HttpServletRequest request) {
        String id = getUserIdFromJwtCookie(request);
        List<Order> orderList = orderRepository.findByUserId(id);
        if (!orderList.isEmpty()) {
            return orderList;
        } else {
            throw new RuntimeException("You do not have any orders");
        }
    }

    private String getUserIdFromJwtCookie(HttpServletRequest request) {
        String token = "";
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("jwt")) {
                token = cookie.getValue();
                break;
            }
        }
        String username = jwtUtils.extractUsername(token);
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with username: " + username);
        }
        return userOptional.get().getId();
    }
}
