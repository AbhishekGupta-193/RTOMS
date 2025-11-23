package eatclub.rtoms.DTO;

import org.junit.jupiter.api.Test;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemRequestTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        OrderItemRequest req = new OrderItemRequest();

        UUID productId = UUID.randomUUID();
        int quantity = 5;

        req.setProductId(productId);
        req.setQuantity(quantity);

        assertEquals(productId, req.getProductId());
        assertEquals(quantity, req.getQuantity());
    }

    @Test
    void testAllArgsConstructor() {
        UUID productId = UUID.randomUUID();
        int quantity = 10;

        OrderItemRequest req = new OrderItemRequest(productId, quantity);

        assertEquals(productId, req.getProductId());
        assertEquals(quantity, req.getQuantity());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();

        OrderItemRequest req1 = new OrderItemRequest(id, 3);
        OrderItemRequest req2 = new OrderItemRequest(id, 3);

        assertEquals(req1, req2);
        assertEquals(req1.hashCode(), req2.hashCode());
    }

    @Test
    void testToString() {
        OrderItemRequest req = new OrderItemRequest(UUID.randomUUID(), 4);

        String str = req.toString();

        assertNotNull(str);
        assertTrue(str.contains("productId"));
        assertTrue(str.contains("quantity"));
    }
}
