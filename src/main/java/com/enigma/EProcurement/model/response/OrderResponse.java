package com.enigma.EProcurement.model.response;

import com.enigma.EProcurement.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class OrderResponse {

    private String orderId;
    private LocalDateTime transDate;
    private List<OrderDetailResponse> orderDetails;

}