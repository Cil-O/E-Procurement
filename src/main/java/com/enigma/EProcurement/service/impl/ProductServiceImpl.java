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
import com.enigma.EProcurement.service.ProductService;
import com.enigma.EProcurement.service.VendorService;
import com.enigma.EProcurement.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final VendorService vendorService;
    private final ProductPriceService productPriceService;
    private final ValidationUtil validationUtil;
    private final CategoryService categoryService;

    @Transactional(rollbackOn = Exception.class)
    @Override
    public ProductResponse create(ProductRequest request) {
        validationUtil.validate(request);
        Vendor vendor = vendorService.getById(request.getVendorId());
        Category category = categoryService.getById(request.getCategoryId());

        Product product = Product.builder()
                .name(request.getProductName())
                .description(request.getDescription())
                .category(category)
                .build();
        productRepository.saveAndFlush(product);

        ProductPrice productPrice = ProductPrice.builder()
                .price(request.getPrice())
                .stock(request.getStock())
                .vendor(vendor)
                .product(product)
                .isActive(true)
                .build();
        productPriceService.create(productPrice);

        return ProductResponse.builder()
                .id(product.getId())
                .productName(product.getName())
                .description(product.getDescription())
                .price(productPrice.getPrice())
                .stock(productPrice.getStock())
                .category(category.getName())
                .vendor(VendorResponse.builder()
                        .id(vendor.getId())
                        .name(vendor.getName())
                        .mobilePhone(vendor.getMobilePhone())
                        .build())
                .build();
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public List<ProductResponse> createBulk(List<ProductRequest> products) {
        return products.stream().map(this::create).collect(Collectors.toList());
    }

    @Override
    public ProductResponse getById(String id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "product not found"));
        Optional<ProductPrice> productPrice = product.getProductPrices().stream().filter(ProductPrice::getIsActive).findFirst();

        if (productPrice.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "active product price not found");
        Vendor vendor = productPrice.get().getVendor();

        return toProductResponse(product, productPrice.get(), vendor);
    }

    @Override
    public Page<ProductResponse> getAllByNameOrPrice(String name, Long maxPrice, Integer page, Integer size) {
        Specification<Product> specification = (root, query, criteriaBuilder) -> {
            Join<Product, ProductPrice> productPrices = root.join("productPrices");
            List<Predicate> predicates = new ArrayList<>();
            if (name != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(productPrices.get("price"), maxPrice));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productRepository.findAll(specification, pageable);
        List<ProductResponse> productResponses = new ArrayList<>();
        for (Product product : products.getContent()) {
            Optional<ProductPrice> productPrice = product.getProductPrices()
                    .stream()
                    .filter(ProductPrice::getIsActive).findFirst();

            if (productPrice.isEmpty()) continue;
            Vendor vendor = productPrice.get().getVendor();

            productResponses.add(toProductResponse(product, productPrice.get(), vendor));
        }

        return new PageImpl<>(productResponses, pageable, products.getTotalElements());
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public ProductResponse update(ProductRequest request) {

        Category currentCategory = categoryService.getById(request.getCategoryId());
        Product currentProduct = findByIdOrThrowNotFound(request.getProductId());
        currentProduct.setName(request.getProductName());
        currentProduct.setDescription(request.getDescription());
        currentProduct.setCategory(currentCategory);

        ProductPrice productPriceActive = productPriceService.findProductPriceActive(request.getProductId(), true);

        if (!productPriceActive.getVendor().getId().equals(request.getVendorId()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "vendor tidak boleh diubah");

        if (!request.getPrice().equals(productPriceActive.getPrice())) {
            productPriceActive.setIsActive(false);
            ProductPrice productPrice = productPriceService.create(ProductPrice.builder()
                    .price(request.getPrice())
                    .stock(request.getStock())
                    .product(currentProduct)
                    .vendor(productPriceActive.getVendor())
                    .isActive(true)
                    .build());
            currentProduct.addProductPrice(productPrice);
            return toProductResponse(currentProduct, productPrice, productPrice.getVendor());
        }

        productPriceActive.setStock(request.getStock());

        return toProductResponse(currentProduct, productPriceActive, productPriceActive.getVendor());
    }

    @Override
    public void deleteById(String id) {
        Product product = findByIdOrThrowNotFound(id);
        productRepository.delete(product);
    }

    private Product findByIdOrThrowNotFound(String id) {
        return productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "product not found"));
    }

    private static ProductResponse toProductResponse(Product product, ProductPrice productPrice, Vendor vendor) {
        return ProductResponse.builder()
                .id(product.getId())
                .productName(product.getName())
                .description(product.getDescription())
                .category(product.getCategory().getId())
                .price(productPrice.getPrice())
                .stock(productPrice.getStock())
                .vendor(VendorResponse.builder()
                        .id(vendor.getId())
                        .name(vendor.getName())
                        .mobilePhone(vendor.getMobilePhone())
                        .build())
                .build();
    }
}
