package com.ecommerce.ecomApp.service;

import com.ecommerce.ecomApp.dto.ProductDto;
import com.ecommerce.ecomApp.response.Response;

import java.util.List;

public interface ProductService {

    Response<List<ProductDto>> findAllProducts();

    Response<ProductDto> findProductById(Long id);

    Response<ProductDto> saveProduct(ProductDto productDto);

    Response<List<ProductDto>> saveManyProducts(List<ProductDto> productDtos);

    Response<ProductDto> deleteProduct(String id);
}
