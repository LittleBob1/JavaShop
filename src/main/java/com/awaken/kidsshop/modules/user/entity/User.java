package com.awaken.kidsshop.modules.user.entity;

import com.awaken.kidsshop.modules.role.entity.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min=2, message = "Не меньше 5 знаков")
    @NotNull
    @NotBlank
    private String username;

    @Size(min=2, message = "Не меньше 5 знаков")
    @NotNull
    @NotBlank
    private String password;
    
    @NotNull
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    public void setUsername(@Size(min = 2, message = "Не меньше 5 знаков") @NotNull @NotBlank String username) {
        this.username = username;
    }

    public void setPassword(@Size(min = 2, message = "Не меньше 5 знаков") @NotNull @NotBlank String password) {
        this.password = password;
    }

    public @NotNull Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(@NotNull Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
