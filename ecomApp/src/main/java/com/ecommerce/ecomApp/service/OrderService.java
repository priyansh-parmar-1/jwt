package com.ecommerce.ecomApp.service;

import com.ecommerce.ecomApp.entity.Order;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface OrderService {

    Order createOrder(Order order,HttpServletRequest request);

    List<Order> findAllOrders();

    List<Order> findOrdersByUserId(HttpServletRequest request);
}
