package com.enigma.EProcurement.service.impl;

import com.enigma.EProcurement.entity.Vendor;
import com.enigma.EProcurement.model.response.VendorResponse;
import com.enigma.EProcurement.repository.AdminRepository;
import com.enigma.EProcurement.repository.VendorRepository;
import com.enigma.EProcurement.service.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {
    private final VendorRepository vendorRepository;

    @Override
    public Vendor create(Vendor vendor) {

        try {
            return vendorRepository.save(vendor);
        } catch (DataIntegrityViolationException exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "username already exist");
        }
    }

    @Override
    public Vendor getById(String id) {
        return vendorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Store not found"));
    }

    @Override
    public List<Vendor> getAll() {
        return vendorRepository.findAll();
    }

    @Override
    public VendorResponse getVendorByName(String vendorName) {
        Vendor vendor = vendorRepository.findByName(vendorName);
        if (vendor == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vendor not found");
        }

        return VendorResponse.builder()
                .id(vendor.getId())
                .name(vendor.getName())
                .mobilePhone(vendor.getMobilePhone())
                .build();
    }
}
