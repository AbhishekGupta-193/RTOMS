package eatclub.rtoms.Entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void testSettersAndGetters() {
        Product p = new Product();
        UUID id = UUID.randomUUID();
        Restaurant r = new Restaurant();
        String name = "Pizza";
        double price = 250.5;
        LocalDateTime now = LocalDateTime.now();

        p.setProductId(id);
        p.setRestaurant(r);
        p.setName(name);
        p.setPrice(price);
        p.setCreatedAt(now);

        assertEquals(id, p.getProductId());
        assertEquals(r, p.getRestaurant());
        assertEquals(name, p.getName());
        assertEquals(price, p.getPrice());
        assertEquals(now, p.getCreatedAt());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        Restaurant r = new Restaurant();
        LocalDateTime createdAt = LocalDateTime.now();

        Product p1 = new Product(id, r, "Burger", 100, createdAt);
        Product p2 = new Product(id, r, "Burger", 100, createdAt);

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());

        Product p3 = new Product(UUID.randomUUID(), r, "Burger", 100, createdAt);
        assertNotEquals(p1, p3);
    }

    @Test
    void testToStringContainsFields() {
        UUID id = UUID.randomUUID();
        Restaurant r = new Restaurant();
        Product p = new Product(id, r, "Burger", 120, LocalDateTime.now());

        String s = p.toString();
        assertTrue(s.contains(id.toString()));
        assertTrue(s.contains("Burger"));
        assertTrue(s.contains("120"));
    }

    @Test
    void testNullFields() {
        Product p = new Product();
        p.setProductId(null);
        p.setRestaurant(null);
        p.setName(null);
        p.setPrice(0);
        p.setCreatedAt(null);

        assertNull(p.getProductId());
        assertNull(p.getRestaurant());
        assertNull(p.getName());
        assertEquals(0, p.getPrice());
        assertNull(p.getCreatedAt());
    }
}
