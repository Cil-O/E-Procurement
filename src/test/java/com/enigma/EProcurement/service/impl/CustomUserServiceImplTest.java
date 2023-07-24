package com.enigma.EProcurement.service.impl;

import com.enigma.EProcurement.entity.Role;
import com.enigma.EProcurement.entity.UserCredential;
import com.enigma.EProcurement.entity.constant.ERole;
import com.enigma.EProcurement.repository.UserCredentialRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserServiceImplTest {

    @Mock
    private UserCredentialRepository userCredentialRepository;

    private CustomUserServiceImpl customUserService;

    private UserCredential userCredential;

    @BeforeEach
    void setUp() {
        customUserService = new CustomUserServiceImpl(userCredentialRepository);

        userCredential = new UserCredential();
        userCredential.setEmail("user@example.com");
        userCredential.setPassword("password");

        Role role1 = new Role();
        role1.setId("1L");
        role1.setRole(ERole.ROLE_ADMIN);
        Role role2 = new Role();
        role2.setId("2L");
        role2.setRole(ERole.ROLE_SUPER_ADMIN);

        List<Role> roles = new ArrayList<>();
        roles.add(role1);
        roles.add(role2);

        userCredential.setRoles(roles);
    }

    @Test
    void loadUserByUsername_ExistingUser_ReturnsUserDetails() {
        // Arrange
        when(userCredentialRepository.findByEmail(anyString())).thenReturn(Optional.of(userCredential));

        // Act
        UserDetails userDetails = customUserService.loadUserByUsername("user@example.com");

        // Assert
        Assertions.assertEquals("user@example.com", userDetails.getUsername());
        Assertions.assertEquals("password", userDetails.getPassword());

        List<GrantedAuthority> expectedAuthorities = List.of(
                new SimpleGrantedAuthority("ROLE_ADMIN"),
                new SimpleGrantedAuthority("ROLE_SUPER_ADMIN")
        );
        Assertions.assertEquals(expectedAuthorities, userDetails.getAuthorities());
    }

    @Test
    void loadUserByUsername_UserNotFound_ThrowsUsernameNotFoundException() {
        // Arrange
        when(userCredentialRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Act/Assert
        Assertions.assertThrows(org.springframework.security.core.userdetails.UsernameNotFoundException.class, () -> {
            customUserService.loadUserByUsername("user@example.com");
        });
    }
}
