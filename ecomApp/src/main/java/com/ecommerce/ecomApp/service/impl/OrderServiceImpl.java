package com.ecommerce.ecomApp.service.impl;

import com.ecommerce.ecomApp.entity.*;
import com.ecommerce.ecomApp.jwt.JwtUtils;
import com.ecommerce.ecomApp.repository.OrderRepository;
import com.ecommerce.ecomApp.repository.ProductRepository;
import com.ecommerce.ecomApp.repository.UserRepository;
import com.ecommerce.ecomApp.response.Response;
import com.ecommerce.ecomApp.response.ResponseConstants;
import com.ecommerce.ecomApp.service.OrderService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
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
    public Response<Order> createOrder(Order order, HttpServletRequest request) {
        log.info("IN ORDER-SERVICE :: createOrder");
        if (order != null) {
            double totalPrice = 0.0;
            for (OrderItem orderItem : order.getOrderItems()) {
                Optional<Product> productOpt = productRepository.findById(orderItem.getProductId());

                if (productOpt.isEmpty()) {
                    log.error("IN ORDER-SERVICE :: createOrder :: productOpt is empty");
                    return Response.<Order>builder()
                            .status(ResponseConstants.ERROR)
                            .error("Product not found: " + orderItem.getProductId())
                            .build();
                }

                Product product = productOpt.get();
                if (product.getStock() < orderItem.getQuantity()) {
                    log.error("IN ORDER-SERVICE :: createOrder :: Not enough stock");
                    return Response.<Order>builder()
                            .status(ResponseConstants.ERROR)
                            .error("Insufficient stock for product: " + product.getName())
                            .build();
                }

                totalPrice += orderItem.getQuantity() * product.getPrice();

                product.setStock(product.getStock() - orderItem.getQuantity());
                productRepository.save(product);
                log.info("IN ORDER-SERVICE :: createOrder :: product {} stock updated to {}", product.getName(), product.getStock());
            }
            String id = getUserIdFromJwtCookie(request);
            order.setUserId(id);
            order.setTotalPrice(totalPrice);
            order.setStatus(OrderStatus.CONFIRM);
            order.setOrderDate(String.valueOf(new Date()));
            try {
                Order savedOrder = orderRepository.save(order);
                log.info("IN ORDER-SERVICE :: createOrder :: savedOrder {}", savedOrder);
                return Response.<Order>builder()
                        .status(ResponseConstants.SUCCESS)
                        .data(savedOrder)
                        .build();
            } catch (Exception e) {
                log.error("IN ORDER-SERVICE :: createOrder :: error {}", e.getMessage());
                return Response.<Order>builder()
                        .status(ResponseConstants.ERROR)
                        .error(e.getMessage())
                        .build();
            }
        } else {
            return Response.<Order>builder()
                    .status(ResponseConstants.ERROR)
                    .error("Null order")
                    .build();
        }
    }


    @Override
    public Response<List<Order>> findAllOrders() {
        log.info("IN ORDER-SERVICE :: findAllOrders");
        List<Order> allOrders = orderRepository.findAll();
        if (allOrders.isEmpty()) {
            log.error("IN ORDER-SERVICE :: findAllOrders :: no orders found");
            return Response.<List<Order>>builder()
                    .status(ResponseConstants.ERROR)
                    .error("No orders found")
                    .build();
        } else {
            log.info("IN ORDER-SERVICE :: findAllOrders :: orders found");
            return Response.<List<Order>>builder()
                    .status(ResponseConstants.SUCCESS)
                    .data(allOrders)
                    .build();
        }
    }

    @Override
    public Response<List<Order>> findOrdersByUserId(HttpServletRequest request) {
        log.info("IN ORDER-SERVICE :: findOrdersByUserId");
        String id = getUserIdFromJwtCookie(request);
        log.info("IN ORDER-SERVICE :: findOrdersByUserId :: id {}", id);
        List<Order> orderList = orderRepository.findByUserId(id);
        if (!orderList.isEmpty()) {
            log.info("IN ORDER-SERVICE :: findOrdersByUserId :: orders found");
            return Response.<List<Order>>builder()
                    .status(ResponseConstants.SUCCESS)
                    .data(orderList)
                    .build();
        } else {
            log.error("IN ORDER-SERVICE :: findOrdersByUserId :: Dont have any orders");
            return Response.<List<Order>>builder()
                    .status(ResponseConstants.ERROR)
                    .error("You do not have any orders")
                    .build();
        }
    }

    @Override
    public Response<Order> updateOrder(String id, String status) {
        log.info("IN ORDER-SERVICE :: updateOrder");
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            log.info("IN ORDER-SERVICE :: updateOrder :: order {}", order);
            EnumSet<OrderStatus> orderStatusSet = EnumSet.allOf(OrderStatus.class);
            boolean validStatus = false;
            for (OrderStatus orderStatus : orderStatusSet) {
                if (orderStatus.name().equalsIgnoreCase(status)) {
                    order.setStatus(orderStatus);
                    validStatus = true;
                    break;
                }
            }
            if (!validStatus) {
                log.error("IN ORDER-SERVICE :: updateOrder :: orderStatus is not valid");
                return Response.<Order>builder()
                        .status(ResponseConstants.ERROR)
                        .error("Order status is not valid")
                        .build();
            }
            orderRepository.save(order);
            log.info("IN ORDER-SERVICE :: updateOrder :: order {}", order);
            return Response.<Order>builder()
                    .status(ResponseConstants.SUCCESS)
                    .data(order)
                    .build();
        } else {
            log.error("IN ORDER-SERVICE :: updateOrder :: Order not found");
            return Response.<Order>builder()
                    .status(ResponseConstants.ERROR)
                    .error("Order not found")
                    .build();
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
