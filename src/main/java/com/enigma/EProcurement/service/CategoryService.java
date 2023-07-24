package com.enigma.EProcurement.service;

import com.enigma.EProcurement.entity.Category;
import com.enigma.EProcurement.entity.Vendor;

public interface CategoryService {
    Category create(Category category);
    Category getById(String id);
}
