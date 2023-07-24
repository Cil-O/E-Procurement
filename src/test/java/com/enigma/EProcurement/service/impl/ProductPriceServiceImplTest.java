package com.enigma.EProcurement.service.impl;

import com.enigma.EProcurement.entity.Product;
import com.enigma.EProcurement.entity.ProductPrice;
import com.enigma.EProcurement.entity.Vendor;
import com.enigma.EProcurement.repository.ProductPriceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


public class ProductPriceServiceImplTest {

    @Mock
    private ProductPriceRepository productPriceRepository;

    @InjectMocks
    private ProductPriceServiceImpl productPriceService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateProductPrice() {
        ProductPrice productPrice = new ProductPrice();
        productPrice.setId("1");
        productPrice.setPrice(1000L);
        productPrice.setStock(10);
        productPrice.setVendor(new Vendor());

        when(productPriceRepository.save(any(ProductPrice.class))).thenReturn(productPrice);

        ProductPrice result = productPriceService.create(productPrice);
        Assertions.assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals(1000L, result.getPrice());
        assertEquals(10, result.getStock());
    }

    @Test
    public void testGetByIdExistingProductPrice() {
        Product product = new Product();
        product.setId("product-1");

        ProductPrice productPrice = new ProductPrice();
        productPrice.setId("1");
        productPrice.setProduct(product);
        productPrice.setPrice(1000L);
        productPrice.setStock(10);
        productPrice.setVendor(new Vendor());

        when(productPriceRepository.findById("1")).thenReturn(Optional.of(productPrice));

        ProductPrice result = productPriceService.getById("1");
        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals(product, result.getProduct());
        assertEquals(1000L, result.getPrice());
        assertEquals(10, result.getStock());
    }

    @Test
    public void testGetByIdNonExistingProductPrice() {
        when(productPriceRepository.findById("1")).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> productPriceService.getById("1"));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("product not found", exception.getReason());
    }

    @Test
    public void testFindProductPriceActiveExisting() {
        Product product = new Product();
        product.setId("product-1");

        ProductPrice productPrice = new ProductPrice();
        productPrice.setId("1");
        productPrice.setProduct(product);
        productPrice.setPrice(1000L);
        productPrice.setStock(10);
        productPrice.setVendor(new Vendor());
        productPrice.setIsActive(true);

        when(productPriceRepository.findByProduct_IdAndIsActive("product-1", true)).thenReturn(Optional.of(productPrice));

        ProductPrice result = productPriceService.findProductPriceActive("product-1", true);
        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals(product, result.getProduct());
        assertEquals(1000L, result.getPrice());
        assertEquals(10, result.getStock());
        assertTrue(result.getIsActive());
    }

    @Test
    public void testFindProductPriceActiveNonExisting() {
        when(productPriceRepository.findByProduct_IdAndIsActive("product-1", true)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> productPriceService.findProductPriceActive("product-1", true));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("product not found", exception.getReason());
    }

}
