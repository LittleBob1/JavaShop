package com.awaken.kidsshop.modules.order.controller.dto.response;

import com.awaken.kidsshop.modules.buyer.entity.Buyer;
import com.awaken.kidsshop.modules.orderitem.entity.OrderItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    @Schema(description = "Identification of order")
    public Long id;

    private LocalDateTime salesDate;
    private String status;
    private Buyer buyer;
    private String username;
    private Set<OrderItem> orderItems;
}
