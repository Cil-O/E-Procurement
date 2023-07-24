package com.enigma.EProcurement.service.impl;

import com.enigma.EProcurement.entity.Vendor;
import com.enigma.EProcurement.model.response.VendorResponse;
import com.enigma.EProcurement.repository.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VendorServiceImplTest {

    @Mock
    private VendorRepository vendorRepository;

    @InjectMocks
    private VendorServiceImpl vendorService;

    private Vendor vendor;

    @BeforeEach
    public void setUp() {
        vendor = new Vendor();
        vendor.setId("vendor-1");
        vendor.setName("Vendor A");
        vendor.setMobilePhone("123456789");
    }

    @Test
    public void shouldReturnCreatedVendor() {
        // Given
        when(vendorRepository.save(any(Vendor.class))).thenReturn(vendor);

        // When
        Vendor result = vendorService.create(vendor);

        // Then
        assertNotNull(result);
        assertEquals(vendor.getId(), result.getId());
        assertEquals(vendor.getName(), result.getName());
        assertEquals(vendor.getMobilePhone(), result.getMobilePhone());

        //Verify
        verify(vendorRepository, times(1)).save(any(Vendor.class));
    }

    @Test
    public void shouldThrowConflictExceptionWhenCreatingVendorWithExistingUsername() {
        // Given
        when(vendorRepository.save(any(Vendor.class))).thenThrow(DataIntegrityViolationException.class);

        // When and Then
        assertThrows(ResponseStatusException.class, () -> vendorService.create(vendor));
        verify(vendorRepository, times(1)).save(any(Vendor.class));
    }

    @Test
    public void shouldReturnExistingVendorById() {
        // Given
        when(vendorRepository.findById("vendor-1")).thenReturn(Optional.of(vendor));

        // When
        Vendor result = vendorService.getById("vendor-1");

        // Then
        assertNotNull(result);
        assertEquals(vendor.getId(), result.getId());
        assertEquals(vendor.getName(), result.getName());
        assertEquals(vendor.getMobilePhone(), result.getMobilePhone());
        verify(vendorRepository, times(1)).findById("vendor-1");
    }

    @Test
    public void shouldThrowNotFoundExceptionWhenGettingNonExistingVendorById() {
        // Given
        when(vendorRepository.findById("vendor-2")).thenReturn(Optional.empty());

        // When and Then
        assertThrows(ResponseStatusException.class, () -> vendorService.getById("vendor-2"));
        verify(vendorRepository, times(1)).findById("vendor-2");
    }

    @Test
    public void shouldReturnListOfAllVendors() {
        // Given
        List<Vendor> vendors = new ArrayList<>();
        vendors.add(vendor);

        // When
        when(vendorRepository.findAll()).thenReturn(vendors);
        List<Vendor> result = vendorService.getAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(vendor.getId(), result.get(0).getId());
        assertEquals(vendor.getName(), result.get(0).getName());
        assertEquals(vendor.getMobilePhone(), result.get(0).getMobilePhone());
        verify(vendorRepository, times(1)).findAll();
    }

    @Test
    public void shouldReturnVendorResponseByName() {
        // Given
        when(vendorRepository.findByName("Vendor A")).thenReturn(vendor);

        // When
        VendorResponse result = vendorService.getVendorByName("Vendor A");

        // Then
        assertNotNull(result);
        assertEquals(vendor.getId(), result.getId());
        assertEquals(vendor.getName(), result.getName());
        assertEquals(vendor.getMobilePhone(), result.getMobilePhone());
        verify(vendorRepository, times(1)).findByName("Vendor A");
    }

    @Test
    public void shouldThrowNotFoundExceptionWhenGettingNonExistingVendorByName() {
        // Given
        when(vendorRepository.findByName("Vendor B")).thenReturn(null);

        // When and Then.
        assertThrows(ResponseStatusException.class, () -> vendorService.getVendorByName("Vendor B"));
        verify(vendorRepository, times(1)).findByName("Vendor B");
    }
}
