package com.enigma.EProcurement.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class VendorResponse {

    private String id;
    private String name;
    private String email;
    private String mobilePhone;
}
