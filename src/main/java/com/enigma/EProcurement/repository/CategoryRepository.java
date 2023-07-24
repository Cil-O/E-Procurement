package com.enigma.EProcurement.repository;

import com.enigma.EProcurement.entity.Category;
import com.enigma.EProcurement.entity.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {
}
