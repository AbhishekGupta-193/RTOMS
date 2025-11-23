package eatclub.rtoms.Entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void testSettersAndGetters() {
        Order order = new Order();
        UUID id = UUID.randomUUID();
        Customer customer = new Customer();
        Restaurant restaurant = new Restaurant();
        double total = 450.5;
        String status = "PLACED";
        LocalDateTime now = LocalDateTime.now();

        order.setOrderId(id);
        order.setCustomer(customer);
        order.setRestaurant(restaurant);
        order.setStatus(status);
        order.setTotalAmount(total);
        order.setCreatedAt(now);

        assertEquals(id, order.getOrderId());
        assertEquals(customer, order.getCustomer());
        assertEquals(restaurant, order.getRestaurant());
        assertEquals(status, order.getStatus());
        assertEquals(total, order.getTotalAmount());
        assertEquals(now, order.getCreatedAt());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        Customer customer = new Customer();
        Restaurant restaurant = new Restaurant();
        Order o1 = new Order(id, customer, restaurant, "PLACED", 100, LocalDateTime.now());
        Order o2 = new Order(id, customer, restaurant, "PLACED", 100, o1.getCreatedAt());

        // Same values -> equals true
        assertEquals(o1, o2);
        assertEquals(o1.hashCode(), o2.hashCode());

        // Different orderId -> not equals
        Order o3 = new Order(UUID.randomUUID(), customer, restaurant, "PLACED", 100, LocalDateTime.now());
        assertNotEquals(o1, o3);
    }

    @Test
    void testToStringContainsFields() {
        UUID id = UUID.randomUUID();
        Customer customer = new Customer();
        Restaurant restaurant = new Restaurant();
        Order order = new Order(id, customer, restaurant, "PLACED", 200, LocalDateTime.now());

        String s = order.toString();
        assertTrue(s.contains(id.toString()));
        assertTrue(s.contains("PLACED"));
        assertTrue(s.contains("200"));
    }

    @Test
    void testNullFields() {
        Order order = new Order();
        order.setOrderId(null);
        order.setCustomer(null);
        order.setRestaurant(null);
        order.setStatus(null);
        order.setTotalAmount(0);
        order.setCreatedAt(null);

        assertNull(order.getOrderId());
        assertNull(order.getCustomer());
        assertNull(order.getRestaurant());
        assertNull(order.getStatus());
        assertEquals(0, order.getTotalAmount());
        assertNull(order.getCreatedAt());
    }
}
