package com.awaken.kidsshop.modules.user.controller.dto.response;

import com.awaken.kidsshop.modules.role.dto.RoleResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    public Long id;
    public String username;
    private Set<RoleResponse> roles;
}
