package com.ecommerce.ecomApp.service.impl;

import com.ecommerce.ecomApp.dto.ProductDto;
import com.ecommerce.ecomApp.entity.Product;
import com.ecommerce.ecomApp.repository.ProductRepository;
import com.ecommerce.ecomApp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<ProductDto> findAllProducts() {
        List<Product> allProducts = productRepository.findAll();
        if (!allProducts.isEmpty()) {
            List<ProductDto> productDtoList = new ArrayList<>();
            allProducts.forEach(product -> {
                ProductDto productDto = new ProductDto();
                productDto.setStock(product.getStock());
                productDto.setName(product.getName());
                productDto.setPrice(product.getPrice());
                productDtoList.add(productDto);
            });
            return productDtoList;
        } else {
            throw new RuntimeException("No products found");
        }
    }

    @Override
    public ProductDto findProductById(Long id) {
        return null;
    }

    @Override
    public ProductDto saveProduct(ProductDto productDto) {
        if (productDto != null) {
            Product product = new Product();
            product.setName(productDto.getName());
            product.setPrice(productDto.getPrice());
            product.setStock(productDto.getStock());
            productRepository.save(product);
            return productDto;
        } else {
            throw new RuntimeException("Empty product details");
        }
    }

    @Override
    public List<ProductDto> saveManyProducts(List<ProductDto> productDtos) {
        if (!productDtos.isEmpty()) {
            List<Product> products = productDtos.stream()
                    .map(productDto -> {
                        Product product = new Product();
                        product.setName(productDto.getName());
                        product.setPrice(productDto.getPrice());
                        product.setStock(productDto.getStock());
                        return product;
                    }).toList();
            productRepository.saveAll(products);
            return productDtos;
        } else {
            throw new RuntimeException("Empty product list");
        }
    }
}
