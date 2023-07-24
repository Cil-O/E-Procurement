package com.enigma.EProcurement.repository;

import com.enigma.EProcurement.entity.Order;
import com.enigma.EProcurement.model.response.VendorResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String>, JpaSpecificationExecutor<Order> {
    List<Order> findByOrderDetails_ProductPrice_Vendor(VendorResponse vendor);
}