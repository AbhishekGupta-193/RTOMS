package eatclub.rtoms.Controllers;

import eatclub.rtoms.DTO.OrderRequest;
import eatclub.rtoms.Entity.Order;
import eatclub.rtoms.Services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/place")
    public ResponseEntity<Order> placeOrder(@RequestBody OrderRequest request) {
        Order order = orderService.placeOrder(request);
        return ResponseEntity.ok(order);
    }

    // Fetch all orders
    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // Fetch orders by customerId
    @GetMapping("/{customerId}")
    public ResponseEntity<List<Order>> getOrdersByCustomer(@PathVariable UUID customerId) {
        return ResponseEntity.ok(orderService.getOrdersByCustomer(customerId));
    }

    // Update Order Status by Restaurant
    @PutMapping("/{orderId}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable UUID orderId,
            @RequestParam UUID restaurantId,
            @RequestParam String status
    ) {
        Order updatedOrder = orderService.updateOrderStatus(orderId, status, restaurantId);
        return ResponseEntity.ok(updatedOrder);
    }

    // Cancel Order by Customer
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancelOrderByCustomer(
            @PathVariable UUID orderId,
            @RequestParam UUID customerId
    ) {
        Order cancelledOrder = orderService.cancelOrderByCustomer(orderId, customerId);
        return ResponseEntity.ok(cancelledOrder);
    }

}
