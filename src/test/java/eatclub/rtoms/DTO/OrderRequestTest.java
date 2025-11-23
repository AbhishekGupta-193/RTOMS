package eatclub.rtoms.DTO;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class OrderRequestTest {

    @Test
    void testAllArgsConstructor() {
        UUID cId = UUID.randomUUID();
        UUID rId = UUID.randomUUID();

        OrderItemRequest item = new OrderItemRequest(UUID.randomUUID(), 5);
        List<OrderItemRequest> items = List.of(item);

        OrderRequest req = new OrderRequest(cId, rId, items);

        assertEquals(cId, req.getCustomerId());
        assertEquals(rId, req.getRestaurantId());
        assertEquals(1, req.getItems().size());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        OrderRequest req = new OrderRequest();

        UUID cId = UUID.randomUUID();
        UUID rId = UUID.randomUUID();
        List<OrderItemRequest> items = new ArrayList<>();

        req.setCustomerId(cId);
        req.setRestaurantId(rId);
        req.setItems(items);

        assertEquals(cId, req.getCustomerId());
        assertEquals(rId, req.getRestaurantId());
        assertEquals(items, req.getItems());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID cId = UUID.randomUUID();
        UUID rId = UUID.randomUUID();

        List<OrderItemRequest> items = List.of(
                new OrderItemRequest(UUID.randomUUID(), 3)
        );

        OrderRequest req1 = new OrderRequest(cId, rId, items);
        OrderRequest req2 = new OrderRequest(cId, rId, items);

        assertEquals(req1, req2);
        assertEquals(req1.hashCode(), req2.hashCode());
    }

    @Test
    void testNotEquals() {
        OrderRequest req1 = new OrderRequest(UUID.randomUUID(), UUID.randomUUID(), List.of());
        OrderRequest req2 = new OrderRequest(UUID.randomUUID(), UUID.randomUUID(), List.of());

        assertNotEquals(req1, req2);
    }

    @Test
    void testToStringContainsFields() {
        UUID cId = UUID.randomUUID();
        UUID rId = UUID.randomUUID();

        OrderRequest req = new OrderRequest(cId, rId, List.of());

        String s = req.toString();

        assertTrue(s.contains(cId.toString()));
        assertTrue(s.contains(rId.toString()));
        assertTrue(s.contains("items"));
    }

    @Test
    void testItemsListMutation() {
        OrderRequest req = new OrderRequest();
        List<OrderItemRequest> items = new ArrayList<>();

        req.setItems(items);
        items.add(new OrderItemRequest(UUID.randomUUID(), 1));

        assertEquals(1, req.getItems().size());
    }

    @Test
    void testNullValues() {
        OrderRequest req = new OrderRequest(null, null, null);

        assertNull(req.getCustomerId());
        assertNull(req.getRestaurantId());
        assertNull(req.getItems());
    }
}
