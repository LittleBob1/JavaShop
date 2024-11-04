package com.awaken.kidsshop.modules.order.controller.dto.request;

import com.awaken.kidsshop.modules.orderitem.controller.dto.OrderItemDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class OrderRequest {
    private LocalDateTime salesDate;

    private String status;

    private Long buyerId;

    private Long userId;

    private Set<OrderItemDTO> orderItems;
}
