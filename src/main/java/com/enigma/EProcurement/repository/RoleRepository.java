package com.enigma.EProcurement.repository;

import com.enigma.EProcurement.entity.Role;
import com.enigma.EProcurement.entity.constant.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByRole(ERole role);
}
