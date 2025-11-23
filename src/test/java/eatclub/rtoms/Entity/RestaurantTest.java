package eatclub.rtoms.Entity;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantTest {

    @Test
    void testGettersSetters() {
        Restaurant r = new Restaurant();
        UUID id = UUID.randomUUID();

        r.setRestaurantId(id);
        r.setName("Cafe");
        r.setAddress("Bangalore");

        assertEquals(id, r.getRestaurantId());
        assertEquals("Cafe", r.getName());
        assertEquals("Bangalore", r.getAddress());
    }

    @Test
    void testAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        Restaurant r = new Restaurant();
        r.setRestaurantId(id);
        r.setName("Bistro");
        r.setAddress("Delhi");

        assertEquals(id, r.getRestaurantId());
        assertEquals("Bistro", r.getName());
        assertEquals("Delhi", r.getAddress());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        Restaurant r1 = new Restaurant();
        r1.setRestaurantId(id);
        Restaurant r2 = new Restaurant();
        r2.setRestaurantId(id);

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());

        Restaurant r3 = new Restaurant();
        r3.setRestaurantId(UUID.randomUUID());
        assertNotEquals(r1, r3);
    }

    @Test
    void testToStringContainsFields() {
        UUID id = UUID.randomUUID();
        Restaurant r = new Restaurant();
        r.setRestaurantId(id);
        r.setName("Cafe");
        r.setAddress("Bangalore");

        String str = r.toString();
        assertTrue(str.contains("Cafe"));
        assertTrue(str.contains("Bangalore"));
        assertTrue(str.contains(id.toString()));
    }

    @Test
    void testNullFields() {
        Restaurant r = new Restaurant();
        r.setRestaurantId(null);
        r.setName(null);
        r.setAddress(null);

        assertNull(r.getRestaurantId());
        assertNull(r.getName());
        assertNull(r.getAddress());
    }
}
