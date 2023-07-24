package com.enigma.EProcurement.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class VendorRequest {
    private String name;
    private String mobilePhone;
    private String email;
}