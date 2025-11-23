package eatclub.rtoms.Services;
import eatclub.rtoms.Entity.*;
import eatclub.rtoms.Exceptions.InventoryException;
import eatclub.rtoms.Exceptions.ProductNotFoundException;
import eatclub.rtoms.Exceptions.StatusException;
import eatclub.rtoms.Repository.*;
import eatclub.rtoms.DTO.OrderRequest;
import eatclub.rtoms.DTO.OrderItemRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;

    public OrderService(OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository,
                        ProductRepository productRepository,
                        InventoryRepository inventoryRepository,
                        CustomerRepository customerRepository,
                        RestaurantRepository restaurantRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
        this.customerRepository = customerRepository;
        this.restaurantRepository = restaurantRepository;
    }

    //Place new order
    @Transactional
    public Order placeOrder(OrderRequest request) {
        // Fetch Customer details if exists
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Fetch Restaurant details if exists
        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        //Creating order
        Order order = new Order();
        order.setCustomer(customer);
        order.setRestaurant(restaurant);
        order.setStatus("PLACED");

        double totalAmount = 0;

        Order savedOrder = orderRepository.save(order);

        var orderItems = new ArrayList<OrderItem>();

        for (OrderItemRequest itemReq : request.getItems()) {

            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException(
                            "Product with ID " + itemReq.getProductId() + " not found"
                    ));

            if(!product.getRestaurant().getRestaurantId().equals(request.getRestaurantId())){
                throw new ProductNotFoundException(
                        "Product does not belong to this restaurant"
                );
            }

            Inventory inventory = inventoryRepository.findByProduct_ProductId(product.getProductId());

            if (inventory == null || inventory.getQuantity() < itemReq.getQuantity()) {
                throw new InventoryException(
                        "Requested quantity (" + itemReq.getQuantity() +
                                ") exceeds available stock (" + inventory.getQuantity() +
                                ") for product " + product.getName()
                );
            }

            // Deduct inventory
            inventory.setQuantity(inventory.getQuantity() - itemReq.getQuantity());
            inventoryRepository.save(inventory);

            // Create order item
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemReq.getQuantity());
            orderItem.setItemPrice(product.getPrice());

            orderItems.add(orderItemRepository.save(orderItem));

            totalAmount += product.getPrice() * itemReq.getQuantity();
        }

        savedOrder.setTotalAmount(totalAmount);
        return orderRepository.save(savedOrder);
    }

    //Get all orders
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    //Get Customer Specific Orders
    public List<Order> getOrdersByCustomer(UUID customerId) {
        customerRepository.findById(customerId)
                .orElseThrow(() -> new ProductNotFoundException("Customer not found with ID: " + customerId));

        return orderRepository.findByCustomer_CustomerId(customerId);
    }

    //Update order status
    @Transactional
    public Order updateOrderStatus(UUID orderId, String newStatus, UUID restaurantId) {

        List<String> allowedStatuses = List.of("PREPARING", "OUT_FOR_DELIVERY", "DELIVERED");

        if (!allowedStatuses.contains(newStatus.toUpperCase())) {
            throw new StatusException("Invalid status. Allowed values are: " + allowedStatuses);
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new StatusException("Order not found with ID: " + orderId));

        if (!order.getRestaurant().getRestaurantId().equals(restaurantId)) {
            throw new StatusException("Restaurant is not authorized to update this order");
        }
        order.setStatus(newStatus.toUpperCase());
        return orderRepository.save(order);
    }

    //cancel order by customer
    @Transactional
    public Order cancelOrderByCustomer(UUID orderId, UUID customerId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ProductNotFoundException("Order not found"));

        if (!order.getCustomer().getCustomerId().equals(customerId)) {
            throw new StatusException("You cannot cancel an order that does not belong to you");
        }

        if (order.getStatus().equalsIgnoreCase("DELIVERED")) {
            throw new StatusException("Delivered orders cannot be cancelled");
        }

        List<OrderItem> orderItems = orderItemRepository.findByOrder_OrderId(orderId);

        for (OrderItem item : orderItems) {

            Inventory inventory = inventoryRepository.findByProduct_ProductId(
                    item.getProduct().getProductId()
            );

            if (inventory != null) {
                inventory.setQuantity(inventory.getQuantity() + item.getQuantity());
                inventoryRepository.save(inventory);
            }
        }

        order.setStatus("CANCELLED");
        return orderRepository.save(order);
    }


}
