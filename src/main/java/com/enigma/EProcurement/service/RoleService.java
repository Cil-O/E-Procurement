package com.enigma.EProcurement.service;

import com.enigma.EProcurement.entity.Role;
import com.enigma.EProcurement.entity.constant.ERole;

public interface RoleService {

    Role getOrSave(ERole role);

}
