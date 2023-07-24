package com.enigma.EProcurement.service;

import com.enigma.EProcurement.entity.Admin;
import com.enigma.EProcurement.entity.Vendor;
import com.enigma.EProcurement.model.response.VendorResponse;

import java.util.List;

public interface VendorService {
  Vendor create(Vendor vendor);
  Vendor getById(String id);
  List<Vendor> getAll();
  VendorResponse getVendorByName(String vendorName);
}
