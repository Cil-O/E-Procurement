package com.enigma.EProcurement.service.impl;

import com.enigma.EProcurement.entity.Category;
import com.enigma.EProcurement.entity.Product;
import com.enigma.EProcurement.entity.ProductPrice;
import com.enigma.EProcurement.entity.Vendor;
import com.enigma.EProcurement.model.request.ProductRequest;
import com.enigma.EProcurement.model.response.ProductResponse;
import com.enigma.EProcurement.model.response.VendorResponse;
import com.enigma.EProcurement.repository.ProductRepository;
import com.enigma.EProcurement.service.CategoryService;
import com.enigma.EProcurement.service.ProductPriceService;
import com.enigma.EProcurement.service.VendorService;
import com.enigma.EProcurement.util.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private VendorService vendorService;

    @Mock
    private ProductPriceService productPriceService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private ValidationUtil validationUtil;

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductRequest productRequest;
    private Product product;
    private Category category;
    private Vendor vendor;
    private ProductPrice productPrice;

    @BeforeEach
    public void setup() {
        productRequest = new ProductRequest();
        productRequest.setProductName("Product A");
        productRequest.setDescription("Product A Description");
        productRequest.setVendorId("vendor-1");
        productRequest.setCategoryId("category-1");
        productRequest.setPrice(1000L);
        productRequest.setStock(10);

        vendor = new Vendor();
        vendor.setId("vendor-1");
        vendor.setName("Vendor A");
        vendor.setMobilePhone("123456789");
        vendor.setEmail("vendor@example.com");

        category = new Category();
        category.setId("category-1");
        category.setName("Category A");

        product = new Product();
        product.setId("product-1");
        product.setName(productRequest.getProductName());
        product.setDescription(productRequest.getDescription());
        product.setCategory(category);

        productPrice = new ProductPrice();
        productPrice.setId("price-1");
        productPrice.setProduct(product);
        productPrice.setPrice(productRequest.getPrice());
        productPrice.setStock(productRequest.getStock());
        productPrice.setVendor(vendor);
        productPrice.setIsActive(true);
    }

    @Test
    public void shouldReturnProductWhenCreate() {
        // Arrange
        doNothing().when(validationUtil).validate(any(ProductRequest.class));when(vendorService.getById(productRequest.getVendorId())).thenReturn(vendor);
        when(categoryService.getById(productRequest.getCategoryId())).thenReturn(category);
        when(productRepository.saveAndFlush(any(Product.class))).thenReturn(product);
        when(productPriceService.create(any(ProductPrice.class))).thenReturn(productPrice);

        // Act
        ProductResponse result = productService.create(productRequest);

        // Assert
        assertNotNull(result);
        assertEquals(product.getId(), result.getId());
        assertEquals(productRequest.getProductName(), result.getProductName());
        assertEquals(productRequest.getDescription(), result.getDescription());
        assertEquals(productRequest.getPrice(), result.getPrice());
        assertEquals(productRequest.getStock(), result.getStock());
        assertEquals(category.getName(), result.getCategory());
        assertNotNull(result.getVendor());
        assertEquals(vendor.getId(), result.getVendor().getId());
        assertEquals(vendor.getName(), result.getVendor().getName());
        assertEquals(vendor.getMobilePhone(), result.getVendor().getMobilePhone());
        assertEquals(vendor.getEmail(), result.getVendor().getEmail());

        verify(validationUtil, times(1)).validate(any(ProductRequest.class));
        verify(vendorService, times(1)).getById(productRequest.getVendorId());
        verify(categoryService, times(1)).getById(productRequest.getCategoryId());
        verify(productRepository, times(1)).saveAndFlush(any(Product.class));
        verify(productPriceService, times(1)).create(any(ProductPrice.class));
    }

    @Test
    public void shouldReturnProductResponseWhenGetByIdExistingProduct() {
        // Arrange
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(productPriceService.findProductPriceActive(product.getId(), true)).thenReturn(productPrice);

        // Act
        ProductResponse result = productService.getById(product.getId());

        // Assert
        assertNotNull(result);
        assertEquals(product.getId(), result.getId());
        assertEquals(product.getName(), result.getProductName());
        assertEquals(product.getDescription(), result.getDescription());
        assertEquals(productPrice.getPrice(), result.getPrice());
        assertEquals(productPrice.getStock(), result.getStock());
        assertEquals(category.getId(), result.getCategory());
        assertNotNull(result.getVendor());
        assertEquals(vendor.getId(), result.getVendor().getId());
        assertEquals(vendor.getName(), result.getVendor().getName());
        assertEquals(vendor.getMobilePhone(), result.getVendor().getMobilePhone());
        assertEquals(vendor.getEmail(), result.getVendor().getEmail());

        verify(productRepository, times(1)).findById(product.getId());
        verify(productPriceService, times(1)).findProductPriceActive(product.getId(), true);
    }

    @Test
    public void shouldThrowExceptionWhenGetByIdNonExistingProduct() {
        // Arrange
        when(productRepository.findById("non-existing-id")).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> productService.getById("non-existing-id"));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("product not found", exception.getReason());

        verify(productRepository, times(1)).findById("non-existing-id");
        verify(productPriceService, times(0)).findProductPriceActive(any(), anyBoolean());
    }

    @Test
    public void shouldReturnAllProductsByNameOrPrice() {
        // Arrange
        String productName = "Product";
        Long maxPrice = 1500L;
        Integer page = 0;
        Integer size = 10;
        List<Product> productList = Collections.singletonList(product);
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = new PageImpl<>(productList, pageable, productList.size());

        when(productRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(productPage);
        when(productPriceService.findProductPriceActive(product.getId(), true)).thenReturn(productPrice);

        // Act
        Page<ProductResponse> result = productService.getAllByNameOrPrice(productName, maxPrice, page, size);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());

        ProductResponse productResponse = result.getContent().get(0);
        assertEquals(product.getId(), productResponse.getId());
        assertEquals(product.getName(), productResponse.getProductName());
        assertEquals(product.getDescription(), productResponse.getDescription());
        assertEquals(productPrice.getPrice(), productResponse.getPrice());
        assertEquals(productPrice.getStock(), productResponse.getStock());
        assertEquals(category.getId(), productResponse.getCategory());
        assertNotNull(productResponse.getVendor());
        assertEquals(vendor.getId(), productResponse.getVendor().getId());
        assertEquals(vendor.getName(), productResponse.getVendor().getName());
        assertEquals(vendor.getMobilePhone(), productResponse.getVendor().getMobilePhone());
        assertEquals(vendor.getEmail(), productResponse.getVendor().getEmail());

        verify(productRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        verify(productPriceService, times(1)).findProductPriceActive(product.getId(), true);
    }

    @Test
    public void shouldReturnEmptyPageWhenNoProductsFound() {
        // Arrange
        String productName = "Product";
        Long maxPrice = 1500L;
        Integer page = 0;
        Integer size = 10;
        List<Product> emptyList = Collections.emptyList();
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> emptyPage = new PageImpl<>(emptyList, pageable, emptyList.size());

        when(productRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(emptyPage);

        // Act
        Page<ProductResponse> result = productService.getAllByNameOrPrice(productName, maxPrice, page, size);

        // Assert
        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());

        verify(productRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        verify(productPriceService, times(0)).findProductPriceActive(any(), anyBoolean());
    }

    @Test
    public void shouldReturnUpdatedProduct() {
        // Arrange
        ProductRequest updateRequest = new ProductRequest();
        updateRequest.setProductId(product.getId());
        updateRequest.setProductName("Updated Product");
        updateRequest.setDescription("Updated Product Description");
        updateRequest.setVendorId(vendor.getId());
        updateRequest.setCategoryId(category.getId());
        updateRequest.setPrice(2000L);
        updateRequest.setStock(20);

        Category updatedCategory = new Category();
        updatedCategory.setId("category-2");
        updatedCategory.setName("Updated Category");

        Product updatedProduct = new Product();
        updatedProduct.setId(product.getId());
        updatedProduct.setName(updateRequest.getProductName());
        updatedProduct.setDescription(updateRequest.getDescription());
        updatedProduct.setCategory(updatedCategory);

        ProductPrice updatedProductPrice = new ProductPrice();
        updatedProductPrice.setId("price-2");
        updatedProductPrice.setProduct(updatedProduct);
        updatedProductPrice.setPrice(updateRequest.getPrice());
        updatedProductPrice.setStock(updateRequest.getStock());
        updatedProductPrice.setVendor(vendor);
        updatedProductPrice.setIsActive(true);

        ProductPrice existingProductPrice = new ProductPrice();
        existingProductPrice.setId("price-1");
        existingProductPrice.setProduct(product);
        existingProductPrice.setPrice(productPrice.getPrice());
        existingProductPrice.setStock(productPrice.getStock());
        existingProductPrice.setVendor(vendor);
        existingProductPrice.setIsActive(true);

        when(categoryService.getById(updateRequest.getCategoryId())).thenReturn(updatedCategory);
        when(productRepository.findById(updateRequest.getProductId())).thenReturn(Optional.of(product));
        when(productPriceService.findProductPriceActive(updateRequest.getProductId(), true)).thenReturn(existingProductPrice);
        when(productRepository.saveAndFlush(any(Product.class))).thenReturn(updatedProduct);
        when(productPriceService.create(any(ProductPrice.class))).thenReturn(updatedProductPrice);

        // Act
        ProductResponse result = productService.update(updateRequest);

        // Assert
        assertNotNull(result);
        assertEquals(updatedProduct.getId(), result.getId());
        assertEquals(updateRequest.getProductName(), result.getProductName());
        assertEquals(updateRequest.getDescription(), result.getDescription());
        assertEquals(updateRequest.getPrice(), result.getPrice());
        assertEquals(updateRequest.getStock(), result.getStock());
        assertEquals(updatedCategory.getId(), result.getCategory());
        assertNotNull(result.getVendor());
        assertEquals(vendor.getId(), result.getVendor().getId());
        assertEquals(vendor.getName(), result.getVendor().getName());
        assertEquals(vendor.getMobilePhone(), result.getVendor().getMobilePhone());
        assertEquals(vendor.getEmail(), result.getVendor().getEmail());


        verify(categoryService, times(1)).getById(updateRequest.getCategoryId());
        verify(productRepository, times(1)).findById(updateRequest.getProductId());
        verify(productPriceService, times(1)).findProductPriceActive(updateRequest.getProductId(), true);
        verify(productRepository, times(1)).saveAndFlush(any(Product.class));
        verify(productPriceService, times(1)).create(any(ProductPrice.class));
    }

    @Test
    public void shouldThrowExceptionWhenUpdateWithDifferentVendor() {
        // Arrange
        ProductRequest updateRequest = new ProductRequest();
        updateRequest.setProductId(product.getId());
        updateRequest.setProductName("Updated Product");
        updateRequest.setDescription("Updated Product Description");
        updateRequest.setVendorId("different-vendor"); // Different vendor ID
        updateRequest.setCategoryId(category.getId());
        updateRequest.setPrice(2000L);
        updateRequest.setStock(20);

        when(productRepository.findById(updateRequest.getProductId())).thenReturn(Optional.of(product));
        when(productPriceService.findProductPriceActive(updateRequest.getProductId(), true)).thenReturn(productPrice);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> productService.update(updateRequest));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
        assertEquals("vendor tidak boleh diubah", exception.getReason());

        verify(productRepository, times(1)).findById(updateRequest.getProductId());
        verify(productPriceService, times(1)).findProductPriceActive(updateRequest.getProductId(), true);
    }

    @Test
    public void shouldDeleteProductById() {
        // Arrange
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        // Act
        productService.deleteById(product.getId());

        // Assert
        verify(productRepository, times(1)).findById(product.getId());
        verify(productRepository, times(1)).delete(product);
    }

    @Test
    public void shouldThrowExceptionWhenDeleteNonExistingProduct() {
        // Arrange
        when(productRepository.findById("non-existing-id")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> productService.deleteById("non-existing-id"));
        verify(productRepository, times(1)).findById("non-existing-id");
        verify(productRepository, times(0)).delete(any(Product.class));
    }
}
