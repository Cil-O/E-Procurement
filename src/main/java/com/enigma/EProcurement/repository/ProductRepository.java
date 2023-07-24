package com.enigma.EProcurement.repository;

import com.enigma.EProcurement.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String>, JpaSpecificationExecutor<Product> {

    List<Product> findAllByNameLikeIgnoreCase(String name);

}

