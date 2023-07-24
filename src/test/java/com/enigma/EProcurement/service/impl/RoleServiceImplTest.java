package com.enigma.EProcurement.service.impl;

import com.enigma.EProcurement.entity.Role;
import com.enigma.EProcurement.entity.constant.ERole;
import com.enigma.EProcurement.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Test
    public void shouldGetExistingRole() {
        // Arrange
        ERole roleName = ERole.ROLE_ADMIN;
        Role existingRole = new Role();
        existingRole.setId("1L");
        existingRole.setRole(roleName);

        when(roleRepository.findByRole(roleName)).thenReturn(Optional.of(existingRole));

        // Act
        Role result = roleService.getOrSave(roleName);

        // Assert
        assertEquals(existingRole.getId(), result.getId());
        assertEquals(existingRole.getRole(), result.getRole());
        verify(roleRepository, times(1)).findByRole(roleName);
        verify(roleRepository, times(0)).save(any(Role.class));
    }

    @Test
    public void shouldSaveNewRole() {
        // Arrange
        ERole roleName = ERole.ROLE_ADMIN;
        Role newRole = new Role();
        newRole.setId("2L");
        newRole.setRole(roleName);

        when(roleRepository.findByRole(roleName)).thenReturn(Optional.empty());
        when(roleRepository.save(any(Role.class))).thenReturn(newRole);

        // Act
        Role result = roleService.getOrSave(roleName);

        // Assert
        assertEquals(newRole.getId(), result.getId());
        assertEquals(newRole.getRole(), result.getRole());
        verify(roleRepository, times(1)).findByRole(roleName);
        verify(roleRepository, times(1)).save(any(Role.class));
    }
}
