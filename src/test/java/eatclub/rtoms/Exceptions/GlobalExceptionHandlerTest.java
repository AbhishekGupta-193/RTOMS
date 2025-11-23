package eatclub.rtoms.Exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void testProductNotFoundException() {
        ResponseEntity<Object> response = handler.handleProductNotFound(
                new ProductNotFoundException("Not found")
        );

        Map body = (Map) response.getBody();
        assertEquals("Product Not Found", body.get("error"));
        assertEquals("Not found", body.get("details"));
    }

    @Test
    void testInventoryException() {
        ResponseEntity<Object> response = handler.handleInventoryError(
                new InventoryException("No stock")
        );
        Map body = (Map) response.getBody();
        assertEquals("Inventory Error", body.get("error"));
    }

    @Test
    void testGenericException() {
        ResponseEntity<Object> response = handler.handleGeneric(
                new RuntimeException("Server fail")
        );
        Map body = (Map) response.getBody();
        assertEquals("Internal Error", body.get("error"));
    }
}
