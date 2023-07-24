package com.enigma.EProcurement.service;

import com.enigma.EProcurement.model.request.OrderRequest;
import com.enigma.EProcurement.model.response.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse createNewTransaction(OrderRequest request);

    OrderResponse getOrderById(String id);

    List<OrderResponse> getAllTransaction(String vendorName);

}