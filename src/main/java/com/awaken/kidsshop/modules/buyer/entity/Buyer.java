package com.awaken.kidsshop.modules.buyer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Buyer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    private String name;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number is mandatory")
    @Pattern(regexp = "\\+?[0-9]{10,15}", message = "Invalid phone number format")
    private String phone;

    public @NotBlank(message = "Email is mandatory") @Email(message = "Invalid email format") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "Email is mandatory") @Email(message = "Invalid email format") String email) {
        this.email = email;
    }

    public @NotBlank(message = "Phone number is mandatory") @Pattern(regexp = "\\+?[0-9]{10,15}", message = "Invalid phone number format") String getPhone() {
        return phone;
    }

    public void setPhone(@NotBlank(message = "Phone number is mandatory") @Pattern(regexp = "\\+?[0-9]{10,15}", message = "Invalid phone number format") String phone) {
        this.phone = phone;
    }
}
