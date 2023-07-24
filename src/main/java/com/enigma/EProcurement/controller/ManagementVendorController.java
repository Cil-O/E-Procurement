package com.enigma.EProcurement.controller;

import com.enigma.EProcurement.entity.Vendor;
import com.enigma.EProcurement.model.request.VendorRequest;
import com.enigma.EProcurement.model.response.VendorResponse;
import com.enigma.EProcurement.service.VendorService;
import com.enigma.EProcurement.service.impl.VendorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/management-vendor")
public class ManagementVendorController {
    private final VendorService vendorService;

    @Autowired
    public ManagementVendorController(VendorServiceImpl vendorService) {
        this.vendorService = vendorService;
    }

    @PostMapping
    public ResponseEntity<VendorResponse> createVendor(@RequestBody VendorRequest vendorRequest) {
        Vendor vendor = new Vendor();
        vendor.setName(vendorRequest.getName());
        vendor.setMobilePhone(vendorRequest.getMobilePhone());
        vendor.setEmail(vendorRequest.getEmail());

        Vendor createdVendor = vendorService.create(vendor);

        VendorResponse vendorResponse = new VendorResponse();
        vendorResponse.setId(createdVendor.getId());
        vendorResponse.setName(createdVendor.getName());
        vendorResponse.setMobilePhone(createdVendor.getMobilePhone());

        return new ResponseEntity<>(vendorResponse, HttpStatus.CREATED);
    }
}
