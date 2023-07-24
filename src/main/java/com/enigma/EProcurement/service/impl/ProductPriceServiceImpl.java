package com.enigma.EProcurement.service.impl;

import com.enigma.EProcurement.entity.ProductPrice;
import com.enigma.EProcurement.repository.ProductPriceRepository;
import com.enigma.EProcurement.service.ProductPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ProductPriceServiceImpl implements ProductPriceService {

    private final ProductPriceRepository productPriceRepository;

    @Override
    public ProductPrice create(ProductPrice productPrice) {
        return productPriceRepository.save(productPrice);
    }

    @Override
    public ProductPrice getById(String id) {
        return productPriceRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "product not found"));
    }

    @Override
    public ProductPrice findProductPriceActive(String productId, Boolean active) {
        return productPriceRepository.findByProduct_IdAndIsActive(productId, active).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "product not found"));
    }

}