package com.ecommerce.ecomApp.controller;

import com.ecommerce.ecomApp.dto.ProductDto;
import com.ecommerce.ecomApp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        try{
            return ResponseEntity.ok(productService.findAllProducts());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> addProduct(@RequestBody ProductDto productDto) {
        try{
            return ResponseEntity.ok(productService.saveProduct(productDto));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add-many")
    public ResponseEntity<?> addProductMany(@RequestBody List<ProductDto> productDtos) {
        try{
            return ResponseEntity.ok(productService.saveManyProducts(productDtos));
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
