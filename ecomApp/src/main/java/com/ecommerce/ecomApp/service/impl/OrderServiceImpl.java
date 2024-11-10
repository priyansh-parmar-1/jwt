package com.ecommerce.ecomApp.service.impl;

import com.ecommerce.ecomApp.entity.Order;
import com.ecommerce.ecomApp.entity.OrderItem;
import com.ecommerce.ecomApp.entity.OrderStatus;
import com.ecommerce.ecomApp.entity.Product;
import com.ecommerce.ecomApp.repository.OrderRepository;
import com.ecommerce.ecomApp.repository.ProductRepository;
import com.ecommerce.ecomApp.service.OrderService;
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

    @Override
    public Order createOrder(Order order) {
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
}
