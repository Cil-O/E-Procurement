package com.enigma.EProcurement.service.impl;

import com.enigma.EProcurement.entity.Role;
import com.enigma.EProcurement.entity.constant.ERole;
import com.enigma.EProcurement.repository.RoleRepository;
import com.enigma.EProcurement.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role getOrSave(ERole role) {
        return roleRepository.findByRole(role).orElseGet(() -> roleRepository.save(Role.builder().role(role).build()));
    }
}
