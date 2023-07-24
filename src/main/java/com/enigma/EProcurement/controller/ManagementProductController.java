package com.enigma.EProcurement.controller;

import com.enigma.EProcurement.model.request.ProductRequest;
import com.enigma.EProcurement.model.response.CommonResponse;
import com.enigma.EProcurement.model.response.PagingResponse;
import com.enigma.EProcurement.model.response.ProductResponse;
import com.enigma.EProcurement.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/management-product")
public class ManagementProductController {

    private final ProductService productService;

    @Autowired
    public ManagementProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<?> createNewProduct(@RequestBody ProductRequest request) {
        ProductResponse productResponse = productService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.<ProductResponse>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Successfully create new product")
                        .data(productResponse)
                        .build());
    }

    @GetMapping
    public ResponseEntity<?> getAllProduct(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "maxPrice", required = false) Long maxPrice,
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size
    ) {
        Page<ProductResponse> productResponses = productService.getAllByNameOrPrice(name, maxPrice, page - 1, size);
        PagingResponse pagingResponse = PagingResponse.builder()
                .currentPage(page)
                .totalPage(productResponses.getTotalPages())
                .size(size)
                .build();
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully get all customer")
                        .data(productResponses.getContent())
                        .paging(pagingResponse)
                        .build());
    }
}
