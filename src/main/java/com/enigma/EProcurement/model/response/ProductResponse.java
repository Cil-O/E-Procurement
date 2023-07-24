package com.enigma.EProcurement.model.response;

import com.enigma.EProcurement.entity.Category;
import com.enigma.EProcurement.entity.Vendor;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ProductResponse {
    private String id;
    private String productName;
    private String description;
    private Long price;
    private Integer stock;
    private VendorResponse vendor;
    private String category;

}