package eatclub.rtoms.Entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {

    @Test
    void testSettersAndGetters() {
        Inventory inv = new Inventory();
        Product p = new Product();

        inv.setId(1L);
        inv.setProduct(p);
        inv.setQuantity(10);
        inv.setThreshold(3);

        assertEquals(1L, inv.getId());
        assertEquals(p, inv.getProduct());
        assertEquals(10, inv.getQuantity());
        assertEquals(3, inv.getThreshold());
    }

    @Test
    void testAllArgsConstructor() {
        Product p = new Product();
        Inventory inv = new Inventory(2L, p, 20, 5);

        assertEquals(2L, inv.getId());
        assertEquals(p, inv.getProduct());
        assertEquals(20, inv.getQuantity());
        assertEquals(5, inv.getThreshold());
    }

    @Test
    void testEqualsAndHashCode() {
        Product p = new Product();
        Inventory i1 = new Inventory(1L, p, 10, 2);
        Inventory i2 = new Inventory(1L, p, 10, 2);

        assertEquals(i1, i2);
        assertEquals(i1.hashCode(), i2.hashCode());

        Inventory i3 = new Inventory(2L, p, 10, 2);
        assertNotEquals(i1, i3);
    }

    @Test
    void testToStringContainsFields() {
        Product p = new Product();
        Inventory inv = new Inventory(1L, p, 15, 4);

        String str = inv.toString();
        assertTrue(str.contains("1"));
        assertTrue(str.contains("15"));
        assertTrue(str.contains("4"));
    }

    @Test
    void testNullFields() {
        Inventory inv = new Inventory();
        inv.setId(null);
        inv.setProduct(null);
        inv.setQuantity(0);
        inv.setThreshold(0);

        assertNull(inv.getId());
        assertNull(inv.getProduct());
        assertEquals(0, inv.getQuantity());
        assertEquals(0, inv.getThreshold());
    }
}
