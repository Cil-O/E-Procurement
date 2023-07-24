package com.enigma.EProcurement.service;

import com.enigma.EProcurement.entity.ProductPrice;

public interface ProductPriceService {

    ProductPrice create(ProductPrice productPrice);
    ProductPrice getById(String id);
    ProductPrice findProductPriceActive(String productId, Boolean active);

}
