package com.awaken.kidsshop.modules.order.entity;

import com.awaken.kidsshop.modules.buyer.entity.Buyer;
import com.awaken.kidsshop.modules.orderitem.entity.OrderItem;
import com.awaken.kidsshop.modules.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "orders")
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private LocalDateTime salesDate;
    @NotNull
    @NotBlank
    private String status;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    @NotNull
    private Buyer buyer;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> orderItems;
}