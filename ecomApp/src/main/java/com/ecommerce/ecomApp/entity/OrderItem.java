package com.ecommerce.ecomApp.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderItem {

    private String productId;
    private int quantity;
}
