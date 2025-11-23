package eatclub.rtoms.Entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {

    @Test
    void testSettersAndGetters() {
        OrderItem item = new OrderItem();
        Order order = new Order();
        Product product = new Product();

        item.setId(1L);
        item.setOrder(order);
        item.setProduct(product);
        item.setQuantity(5);
        item.setItemPrice(150.0);

        assertEquals(1L, item.getId());
        assertEquals(order, item.getOrder());
        assertEquals(product, item.getProduct());
        assertEquals(5, item.getQuantity());
        assertEquals(150.0, item.getItemPrice());
    }

    @Test
    void testAllArgsConstructor() {
        Order o = new Order();
        Product p = new Product();
        OrderItem item = new OrderItem(2L, o, p, 3, 200.0);

        assertEquals(2L, item.getId());
        assertEquals(o, item.getOrder());
        assertEquals(p, item.getProduct());
        assertEquals(3, item.getQuantity());
        assertEquals(200.0, item.getItemPrice());
    }

    @Test
    void testEqualsAndHashCode() {
        Order o = new Order();
        Product p = new Product();
        OrderItem i1 = new OrderItem(1L, o, p, 2, 100.0);
        OrderItem i2 = new OrderItem(1L, o, p, 2, 100.0);

        assertEquals(i1, i2);
        assertEquals(i1.hashCode(), i2.hashCode());

        OrderItem i3 = new OrderItem(2L, o, p, 2, 100.0);
        assertNotEquals(i1, i3);
    }

    @Test
    void testToStringContainsFields() {
        Order o = new Order();
        Product p = new Product();
        OrderItem item = new OrderItem(1L, o, p, 2, 50.0);

        String s = item.toString();
        assertTrue(s.contains("1"));
        assertTrue(s.contains("50.0"));
    }

    @Test
    void testNullFields() {
        OrderItem item = new OrderItem();
        item.setId(null);
        item.setOrder(null);
        item.setProduct(null);
        item.setQuantity(0);
        item.setItemPrice(0.0);

        assertNull(item.getId());
        assertNull(item.getOrder());
        assertNull(item.getProduct());
        assertEquals(0, item.getQuantity());
        assertEquals(0.0, item.getItemPrice());
    }
}
