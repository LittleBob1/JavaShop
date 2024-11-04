package com.awaken.kidsshop.modules.role.repository;

import com.awaken.kidsshop.modules.role.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}