package com.ecommerce.ecomApp.service.impl;

import com.ecommerce.ecomApp.dto.ProductDto;
import com.ecommerce.ecomApp.entity.Product;
import com.ecommerce.ecomApp.repository.ProductRepository;
import com.ecommerce.ecomApp.response.Response;
import com.ecommerce.ecomApp.response.ResponseConstants;
import com.ecommerce.ecomApp.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Response<List<ProductDto>> findAllProducts() {
        log.info("IN PRODUCT-SERVICE :: findAllProducts");
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
            log.info("OUT PRODUCT-SERVICE :: findAllProducts");
            return Response.<List<ProductDto>>builder()
                    .status(ResponseConstants.SUCCESS)
                    .data(productDtoList)
                    .build();
        } else {
            log.error("IN PRODUCT-SERVICE :: findAllProducts :: No products found");
            return Response.<List<ProductDto>>builder()
                    .status(ResponseConstants.ERROR)
                    .error("No products found")
                    .build();
        }
    }

    @Override
    public Response<ProductDto> findProductById(Long id) {
        return null;
    }

    @Override
    public Response<ProductDto> saveProduct(ProductDto productDto) {
        log.info("IN PRODUCT-SERVICE :: saveProduct");
        if (productDto != null) {
            if (productRepository.findByName(productDto.getName().trim()).isPresent()) {
                log.error("IN PRODUCT-SERVICE :: saveProduct :: Product already exists");
                return Response.<ProductDto>builder()
                        .status(ResponseConstants.ERROR)
                        .error("Product with name " + productDto.getName().trim() + " already exists")
                        .build();
            }
            Product product = new Product();
            product.setName(productDto.getName());
            product.setPrice(productDto.getPrice());
            product.setStock(productDto.getStock());
            productRepository.save(product);
            log.info("OUT PRODUCT-SERVICE :: saveProduct :: Product saved successfully");
            return Response.<ProductDto>builder()
                    .status(ResponseConstants.SUCCESS)
                    .data(productDto)
                    .build();
        } else {
            log.error("IN PRODUCT-SERVICE :: saveProduct :: Null product");
            return Response.<ProductDto>builder()
                    .status(ResponseConstants.ERROR)
                    .error("Null product")
                    .build();
        }
    }

    @Override
    public Response<List<ProductDto>> saveManyProducts(List<ProductDto> productDtos) {
        if (!productDtos.isEmpty()) {
            log.info("IN PRODUCT-SERVICE :: saveManyProducts");
            List<Product> products = productDtos.stream()
                    .map(productDto -> {
                        Product product = new Product();
                        product.setName(productDto.getName());
                        product.setPrice(productDto.getPrice());
                        product.setStock(productDto.getStock());
                        return product;
                    }).toList();
            productRepository.saveAll(products);
            log.info("OUT PRODUCT-SERVICE :: saveManyProducts :: Saved all products");
            return Response.<List<ProductDto>>builder()
                    .status(ResponseConstants.SUCCESS)
                    .data(productDtos)
                    .build();
        } else {
            log.error("IN PRODUCT-SERVICE :: saveManyProducts :: Empty products list");
            return Response.<List<ProductDto>>builder()
                    .status(ResponseConstants.ERROR)
                    .error("No products found")
                    .build();
        }
    }

    @Override
    public Response<ProductDto> deleteProduct(String id) {
        log.info("IN PRODUCT-SERVICE :: deleteProduct");
        Optional<Product> byId = productRepository.findById(id);
        if (byId.isEmpty()) {
            log.error("IN PRODUCT-SERVICE :: deleteProduct :: Product not found");
            return Response.<ProductDto>builder()
                    .status(ResponseConstants.ERROR)
                    .error("No product found")
                    .build();
        }
        Product product = byId.get();
        ProductDto productDto = ProductDto.builder()
                .name(product.getName())
                .price(product.getPrice())
                .stock(product.getStock())
                .build();
        productRepository.delete(product);
        log.info("OUT PRODUCT-SERVICE :: deleteProduct :: Product deleted successfully");
        return Response.<ProductDto>builder()
                .status(ResponseConstants.SUCCESS)
                .data(productDto)
                .build();
    }
}
