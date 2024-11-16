package com.ecommerce.ecomApp.service;

import com.ecommerce.ecomApp.entity.Order;
import com.ecommerce.ecomApp.response.Response;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface OrderService {

    Response<Order> createOrder(Order order, HttpServletRequest request);

    Response<List<Order>> findAllOrders();

    Response<List<Order>> findOrdersByUserId(HttpServletRequest request);

    Response<Order> updateOrder(String id, String status);
}
