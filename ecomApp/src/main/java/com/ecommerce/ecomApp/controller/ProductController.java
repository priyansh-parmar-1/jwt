package com.ecommerce.ecomApp.controller;

import com.ecommerce.ecomApp.dto.ProductDto;
import com.ecommerce.ecomApp.response.Response;
import com.ecommerce.ecomApp.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public Response<List<ProductDto>> getAllProducts() {
        log.info("IN PRODUCT CONTROLLER :: getAllProducts");
        Response<List<ProductDto>> response = productService.findAllProducts();
        log.info("OUT PRODUCT CONTROLLER :: getAllProducts");
        return response;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public Response<ProductDto> addProduct(@RequestBody ProductDto productDto) {
        log.info("IN PRODUCT CONTROLLER :: addProduct");
        Response<ProductDto> response = productService.saveProduct(productDto);
        log.info("OUT PRODUCT CONTROLLER :: addProduct");
        return response;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add-many")
    public Response<List<ProductDto>> addProductMany(@RequestBody List<ProductDto> productDtos) {
        log.info("IN PRODUCT CONTROLLER :: addProductMany");
        Response<List<ProductDto>> response = productService.saveManyProducts(productDtos);
        log.info("OUT PRODUCT CONTROLLER :: addProductMany");
        return response;
    }
}
