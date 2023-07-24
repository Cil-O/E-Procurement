package com.enigma.EProcurement.controller;

import com.enigma.EProcurement.model.request.OrderRequest;
import com.enigma.EProcurement.model.response.OrderResponse;
import com.enigma.EProcurement.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        OrderResponse response = orderService.createNewTransaction(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable String id) {
        OrderResponse response = orderService.getOrderById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders(@RequestParam(required = false) String vendorName) {
        List<OrderResponse> response = orderService.getAllTransaction(vendorName);
        return ResponseEntity.ok(response);
    }
}
