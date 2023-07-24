package com.enigma.EProcurement.service;

import com.enigma.EProcurement.model.request.ProductRequest;
import com.enigma.EProcurement.model.response.ProductResponse;
import org.springframework.data.domain.Page;

import java.util.List;
public interface ProductService {
    ProductResponse create(ProductRequest request);
    List<ProductResponse> createBulk(List<ProductRequest> products);
    ProductResponse getById(String id);
    Page<ProductResponse> getAllByNameOrPrice(String name, Long maxPrice, Integer page, Integer size);
    ProductResponse update(ProductRequest product);
    void deleteById(String id);

}