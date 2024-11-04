package com.awaken.kidsshop.modules.order.controller;

import com.awaken.kidsshop.modules.order.controller.dto.request.OrderRequest;
import com.awaken.kidsshop.modules.order.controller.dto.response.OrderResponse;
import com.awaken.kidsshop.modules.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    private List<OrderResponse> getAllOrders() {
        return orderService.allOrders();
    }

    @GetMapping("/orders/sortByDate")
    private ResponseEntity<?> getOrdersByDate() {return ResponseEntity.ok(orderService.sortOrdersByDate());}

    @GetMapping("/orders/salesCountByDate")
    private ResponseEntity<?> getSalesCountByDate() {return ResponseEntity.ok(orderService.getSalesCountByDay());}

    @PostMapping("/orders/createNewOrder")
    @Operation(operationId = "create new order using post", summary = "Метод для создания заказа")
    private OrderResponse createNewOrder(@RequestBody OrderRequest orderRequest) {
        return orderService.createOrder(orderRequest);
    }

    @PostMapping("/orders/{orderId}/delete")
    private ResponseEntity<?> deleteOrder(@PathVariable Long orderId) {
        try {
            boolean orderIsDeleted = orderService.deleteOrder(orderId);
            if(orderIsDeleted) {
                return ResponseEntity.ok(true);
            }
            return ResponseEntity.badRequest().body(false);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/orders/{orderId}/update")
    private OrderResponse updateOrder(@PathVariable Long orderId, @RequestBody OrderRequest orderRequest) {
        return orderService.updateOrder(orderId, orderRequest);
    }
}
