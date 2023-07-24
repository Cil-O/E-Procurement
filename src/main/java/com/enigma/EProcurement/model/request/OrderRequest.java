package com.enigma.EProcurement.model.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class OrderRequest {

    private String vendorId;
    private List<OrderDetailRequest> orderDetails;

}
