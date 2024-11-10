package com.ecommerce.ecomApp.service;

import com.ecommerce.ecomApp.entity.Order;

import java.util.List;

public interface OrderService {

    Order createOrder(Order order);

    List<Order> findAllOrders();
}
