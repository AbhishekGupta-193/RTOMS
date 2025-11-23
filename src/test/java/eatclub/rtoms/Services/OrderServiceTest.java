package eatclub.rtoms.Services;

import eatclub.rtoms.DTO.OrderItemRequest;
import eatclub.rtoms.DTO.OrderRequest;
import eatclub.rtoms.Entity.*;
import eatclub.rtoms.Exceptions.InventoryException;
import eatclub.rtoms.Exceptions.ProductNotFoundException;
import eatclub.rtoms.Exceptions.StatusException;
import eatclub.rtoms.Repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock private OrderRepository orderRepository;
    @Mock private OrderItemRepository orderItemRepository;
    @Mock private ProductRepository productRepository;
    @Mock private InventoryRepository inventoryRepository;
    @Mock private CustomerRepository customerRepository;
    @Mock private RestaurantRepository restaurantRepository;

    @InjectMocks
    private OrderService orderService;

    private UUID customerId;
    private UUID restaurantId;
    private UUID productId;
    private Customer customer;
    private Restaurant restaurant;
    private Product product;
    private Inventory inventory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customerId = UUID.randomUUID();
        restaurantId = UUID.randomUUID();
        productId = UUID.randomUUID();

        customer = new Customer();
        customer.setCustomerId(customerId);

        restaurant = new Restaurant();
        restaurant.setRestaurantId(restaurantId);

        product = new Product();
        product.setProductId(productId);
        product.setPrice(100.0);
        product.setRestaurant(restaurant);

        inventory = new Inventory();
        inventory.setQuantity(10);
        inventory.setProduct(product);
    }

    // -----------------------------------------------------------
    // PLACE ORDER - SUCCESS
    // -----------------------------------------------------------
    @Test
    void testPlaceOrderSuccess() {

        OrderRequest request = new OrderRequest();
        request.setCustomerId(customerId);
        request.setRestaurantId(restaurantId);
        request.setItems(List.of(new OrderItemRequest(productId, 2)));

        Order savedOrder = new Order();
        savedOrder.setOrderId(UUID.randomUUID());
        savedOrder.setRestaurant(restaurant);
        savedOrder.setCustomer(customer);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(inventoryRepository.findByProduct_ProductId(productId)).thenReturn(inventory);
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(new OrderItem());

        Order result = orderService.placeOrder(request);

        assertNotNull(result);
        verify(orderRepository, times(2)).save(any());
        verify(orderItemRepository, times(1)).save(any());
    }

    // -----------------------------------------------------------
    // PLACE ORDER - PRODUCT NOT FOUND
    // -----------------------------------------------------------
    @Test
    void testPlaceOrder_ProductNotFound() {
        OrderRequest request = new OrderRequest();
        request.setCustomerId(customerId);
        request.setRestaurantId(restaurantId);
        request.setItems(List.of(new OrderItemRequest(productId, 2)));

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> orderService.placeOrder(request));
    }

    // -----------------------------------------------------------
    // PLACE ORDER - INVENTORY INSUFFICIENT
    // -----------------------------------------------------------
    @Test
    void testPlaceOrder_InventoryException() {
        OrderRequest request = new OrderRequest();
        request.setCustomerId(customerId);
        request.setRestaurantId(restaurantId);
        request.setItems(List.of(new OrderItemRequest(productId, 20)));

        inventory.setQuantity(5);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(orderRepository.save(any())).thenReturn(new Order());
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(inventoryRepository.findByProduct_ProductId(productId)).thenReturn(inventory);

        assertThrows(InventoryException.class, () -> orderService.placeOrder(request));
    }

    // -----------------------------------------------------------
    // GET ALL ORDERS
    // -----------------------------------------------------------
    @Test
    void testGetAllOrders() {
        when(orderRepository.findAll()).thenReturn(List.of(new Order(), new Order()));
        List<Order> orders = orderService.getAllOrders();
        assertEquals(2, orders.size());
    }

    // -----------------------------------------------------------
    // GET ORDERS BY CUSTOMER
    // -----------------------------------------------------------
    @Test
    void testGetOrdersByCustomer() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(orderRepository.findByCustomer_CustomerId(customerId))
                .thenReturn(List.of(new Order()));

        List<Order> orders = orderService.getOrdersByCustomer(customerId);
        assertEquals(1, orders.size());
    }

    @Test
    void testGetOrdersByCustomer_NotFound() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () ->
                orderService.getOrdersByCustomer(customerId));
    }

    // -----------------------------------------------------------
    // UPDATE ORDER STATUS - SUCCESS
    // -----------------------------------------------------------
    @Test
    void testUpdateOrderStatusSuccess() {
        UUID orderId = UUID.randomUUID();

        Order order = new Order();
        order.setOrderId(orderId);
        order.setRestaurant(restaurant);
        order.setStatus("PLACED");

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.updateOrderStatus(orderId, "PREPARING", restaurantId);
        assertEquals("PREPARING", result.getStatus());
    }

    // INVALID STATUS
    @Test
    void testUpdateOrderStatus_InvalidStatus() {
        UUID orderId = UUID.randomUUID();

        assertThrows(StatusException.class,
                () -> orderService.updateOrderStatus(orderId, "FINISHED", restaurantId));
    }

    // ORDER NOT FOUND
    @Test
    void testUpdateOrderStatus_OrderNotFound() {
        when(orderRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(StatusException.class,
                () -> orderService.updateOrderStatus(UUID.randomUUID(), "PREPARING", restaurantId));
    }

    // NOT AUTHORIZED
    @Test
    void testUpdateOrderStatus_UnauthorizedRestaurant() {
        UUID orderId = UUID.randomUUID();

        Order order = new Order();
        order.setOrderId(orderId);
        order.setRestaurant(new Restaurant()); // different restaurant ID
        order.getRestaurant().setRestaurantId(UUID.randomUUID());
        order.setStatus("PLACED");

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(StatusException.class,
                () -> orderService.updateOrderStatus(orderId, "PREPARING", restaurantId));
    }

    // -----------------------------------------------------------
    // CANCEL ORDER - SUCCESS
    // -----------------------------------------------------------
    @Test
    void testCancelOrderByCustomerSuccess() {
        UUID orderId = UUID.randomUUID();

        Order order = new Order();
        order.setOrderId(orderId);
        order.setCustomer(customer);
        order.setStatus("PLACED");

        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(3);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderItemRepository.findByOrder_OrderId(orderId)).thenReturn(List.of(item));
        when(inventoryRepository.findByProduct_ProductId(productId)).thenReturn(inventory);
        when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.cancelOrderByCustomer(orderId, customerId);
        assertEquals("CANCELLED", result.getStatus());
    }

    // CANCEL ORDER – WRONG CUSTOMER
    @Test
    void testCancelOrderByCustomer_Unauthorized() {
        UUID orderId = UUID.randomUUID();

        Order order = new Order();
        order.setOrderId(orderId);
        order.setCustomer(new Customer()); // different customer
        order.getCustomer().setCustomerId(UUID.randomUUID());
        order.setStatus("PLACED");

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(StatusException.class,
                () -> orderService.cancelOrderByCustomer(orderId, customerId));
    }

    // CANCEL ORDER – ALREADY DELIVERED
    @Test
    void testCancelOrderByCustomer_Delivered() {
        UUID orderId = UUID.randomUUID();

        Order order = new Order();
        order.setOrderId(orderId);
        order.setCustomer(customer);
        order.setStatus("DELIVERED");

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(StatusException.class,
                () -> orderService.cancelOrderByCustomer(orderId, customerId));
    }
}
