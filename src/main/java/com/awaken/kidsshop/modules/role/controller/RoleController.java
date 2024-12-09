package com.awaken.kidsshop.modules.role.controller;

import com.awaken.kidsshop.modules.role.controller.dto.RoleResponse;
import com.awaken.kidsshop.modules.role.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/roles")
    private List<RoleResponse> getAllRoles() {
        return roleService.allRoles();
    }
}
