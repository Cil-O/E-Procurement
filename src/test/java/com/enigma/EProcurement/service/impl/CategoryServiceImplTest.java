package com.enigma.EProcurement.service.impl;

import com.enigma.EProcurement.entity.Category;
import com.enigma.EProcurement.repository.CategoryRepository;
import com.enigma.EProcurement.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnCategoryWhenCreate() {
        // Given
        Category category = Category.builder()
                .id("1")
                .name("Electronics")
                .build();

        when(categoryRepository.save(any())).thenReturn(category);

        // When
        Category actualCategory = categoryService.create(category);

        // Then
        assertNotNull(actualCategory);
        assertEquals(category.getId(), actualCategory.getId());
        assertEquals(category.getName(), actualCategory.getName());

        // Verify
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void createShouldThrowConflictException() {
        // Given
        Category category = Category.builder()
                .id("1")
                .name("Electronics")
                .build();

        when(categoryRepository.save(any())).thenThrow(new DataIntegrityViolationException("Duplicate entry"));

        // When/Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            categoryService.create(category);
        });

        // Verify
        verify(categoryRepository, times(1)).save(category);

        // Verify the exception status and message
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        assertEquals("username already exist", exception.getReason());
    }

    @Test
    void shouldReturnCategoryWhenGetById() {
        // Given
        String categoryId = "1";
        Category category = Category.builder()
                .id(categoryId)
                .name("Electronics")
                .build();

        when(categoryRepository.findById(anyString())).thenReturn(Optional.of(category));

        // When
        Category actualCategory = categoryService.getById(categoryId);

        // Then
        assertNotNull(actualCategory);
        assertEquals(category.getId(), actualCategory.getId());
        assertEquals(category.getName(), actualCategory.getName());

        // Verify
        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    void getByIdShouldThrowNotFoundException() {
        // Given
        String categoryId = "1";
        when(categoryRepository.findById(anyString())).thenReturn(Optional.empty());

        // When/Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            categoryService.getById(categoryId);
        });

        // Verify
        verify(categoryRepository, times(1)).findById(categoryId);

        // Verify the exception status and message
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Store not found", exception.getReason());
    }
}