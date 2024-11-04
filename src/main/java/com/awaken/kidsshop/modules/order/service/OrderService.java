package com.awaken.kidsshop.modules.order.service;

import com.awaken.kidsshop.modules.buyer.entity.Buyer;
import com.awaken.kidsshop.modules.buyer.repository.BuyerRepository;
import com.awaken.kidsshop.modules.order.controller.dto.request.OrderRequest;
import com.awaken.kidsshop.modules.order.controller.dto.response.OrderResponse;
import com.awaken.kidsshop.modules.order.entity.Order;
import com.awaken.kidsshop.modules.order.repository.OrderRepository;
import com.awaken.kidsshop.modules.orderitem.entity.OrderItem;
import com.awaken.kidsshop.modules.productitem.entity.ProductItem;
import com.awaken.kidsshop.modules.productitem.repository.ProductItemRepository;
import com.awaken.kidsshop.modules.sales.SalesCountByDay;
import com.awaken.kidsshop.modules.user.entity.User;
import com.awaken.kidsshop.modules.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final BuyerRepository buyerRepository;
    private final UserRepository userRepository;
    private final ProductItemRepository productItemRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, BuyerRepository buyerRepository, UserRepository userRepository, ProductItemRepository productItemRepository) {
        this.productItemRepository = productItemRepository;
        this.buyerRepository = buyerRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    public List<OrderResponse> allOrders(){
        return orderRepository.findAll().stream().map(this::getOrderResponse).toList();
    }

    public List<Order> sortOrdersByDate(){
        return orderRepository.findAllByOrderBySalesDateDesc();
    }

    public List<SalesCountByDay> getSalesCountByDay(){
        return orderRepository.findSalesCountByDay();
    }

    public boolean deleteOrder(Long orderId) {
        if (orderRepository.findById(orderId).isPresent()) {
            orderRepository.deleteById(orderId);
            return true;
        }
        return false;
    }

    public OrderResponse createOrder(OrderRequest request){
        Order order = new Order();
        order.setSalesDate(request.getSalesDate());
        order.setStatus(request.getStatus());

        Buyer buyer = buyerRepository.findById(request.getBuyerId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        order.setBuyer(buyer);
        order.setUser(user);

        getOrderItems(request, order);

        return getOrderResponse(order);
    }

    private OrderResponse getOrderResponse(Order order) {
        Order saveOrder = orderRepository.save(order);
        OrderResponse orderResponse = new OrderResponse();

        orderResponse.setId(saveOrder.getId());
        orderResponse.setStatus(saveOrder.getStatus());
        orderResponse.setBuyer(saveOrder.getBuyer());
        orderResponse.setSalesDate(saveOrder.getSalesDate());
        orderResponse.setUsername(saveOrder.getUser().getUsername());
        orderResponse.setOrderItems(saveOrder.getOrderItems());
        return orderResponse;
    }

    public OrderResponse updateOrder(Long id, OrderRequest orderDTO){
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        order.setSalesDate(orderDTO.getSalesDate());
        order.setStatus(orderDTO.getStatus());

        if (orderDTO.getBuyerId() != null) {
            Buyer buyer = buyerRepository.findById(orderDTO.getBuyerId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Buyer not found"));
            order.setBuyer(buyer);
        }

        if (orderDTO.getUserId() != null) {
            User user = userRepository.findById(orderDTO.getUserId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
            order.setUser(user);
        }

        if (orderDTO.getOrderItems() != null) {
            getOrderItems(orderDTO, order);
        }

        return getOrderResponse(order);
    }

    private void getOrderItems(OrderRequest orderDTO, Order order) {
        if (orderDTO.getOrderItems() != null) {
            Set<OrderItem> orderItems = orderDTO.getOrderItems().stream().map(itemDTO -> {
                OrderItem orderItem = new OrderItem();
                ProductItem productItem = productItemRepository.findById(itemDTO.getProductItemId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ProductItem not found"));
                orderItem.setProductItem(productItem);
                orderItem.setQuantity(itemDTO.getQuantity());
                orderItem.setOrder(order);
                return orderItem;
            }).collect(Collectors.toSet());

            order.getOrderItems().clear();
            order.getOrderItems().addAll(orderItems);

        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ProductItem not found");
        }
    }
}
