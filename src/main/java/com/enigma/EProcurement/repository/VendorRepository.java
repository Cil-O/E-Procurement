package com.enigma.EProcurement.repository;

import com.enigma.EProcurement.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorRepository extends JpaRepository<Vendor, String> {
    Vendor findByName(String vendorName);
}
