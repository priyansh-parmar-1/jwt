package com.ecommerce.ecomApp.service;

import com.ecommerce.ecomApp.dto.ProductDto;

import java.util.List;

public interface ProductService {

    List<ProductDto> findAllProducts();

    ProductDto findProductById(Long id);

    ProductDto saveProduct(ProductDto productDto);

    List<ProductDto> saveManyProducts(List<ProductDto> productDtos);
}
