package eatclub.rtoms.Entity;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void testCustomerGettersSetters() {
        Customer c = new Customer();
        UUID id = UUID.randomUUID();

        c.setCustomerId(id);
        c.setName("John");
        c.setEmail("john@mail.com");
        c.setPhone("9999999999");

        assertEquals(id, c.getCustomerId());
        assertEquals("John", c.getName());
        assertEquals("john@mail.com", c.getEmail());
        assertEquals("9999999999", c.getPhone());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        Customer c1 = new Customer();
        c1.setCustomerId(id);

        Customer c2 = new Customer();
        c2.setCustomerId(id);

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void testToString() {
        Customer c = new Customer();
        c.setName("Test");
        assertTrue(c.toString().contains("Test"));
    }
}
