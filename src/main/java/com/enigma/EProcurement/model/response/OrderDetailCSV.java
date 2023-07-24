package com.enigma.EProcurement.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class OrderDetailCSV {
    private String productId;
    private String orderDate;
    private String vendorName;
    private String productName;
    private String category;
    private Long productPrice;
    private Integer quantity;
    private Long totalAmount;
}