package com.awaken.kidsshop.modules.orderitem.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {

    private Long productItemId;
    private Integer quantity;

}
