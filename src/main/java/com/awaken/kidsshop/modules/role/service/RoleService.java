package com.awaken.kidsshop.modules.role.service;

import com.awaken.kidsshop.modules.role.controller.dto.RoleResponse;
import com.awaken.kidsshop.modules.role.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    public List<RoleResponse> allRoles() {
        return roleRepository.findAll().stream().map(role -> new RoleResponse(role.getId(), role.getName())).toList();
    }
}
