package com.awaken.kidsshop.modules.order.controller;

import com.awaken.kidsshop.modules.order.controller.dto.request.OrderRequest;
import com.awaken.kidsshop.modules.order.controller.dto.response.OrderResponse;
import com.awaken.kidsshop.modules.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/orders/{orderId}/update")
    private OrderResponse updateOrder(@PathVariable Long orderId, @RequestBody OrderRequest orderRequest) {
        return orderService.updateOrder(orderId, orderRequest);
    }

    @PostMapping("/orders/{orderId}/complete")
    private OrderResponse completeOrder(@PathVariable Long orderId) {
        return orderService.completeOrder(orderId);
    }

    @PostMapping("/orders/{orderId}/cancel")
    private OrderResponse cancelOrder(@PathVariable Long orderId) {
        return orderService.cancelOrder(orderId);
    }

    @PostMapping("/orders/{orderId}/return")
    private OrderResponse returnOrder(@PathVariable Long orderId) {
        return orderService.returnOrder(orderId);
    }
}
