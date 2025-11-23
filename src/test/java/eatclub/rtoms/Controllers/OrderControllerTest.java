package eatclub.rtoms.Controllers;

import eatclub.rtoms.DTO.OrderRequest;
import eatclub.rtoms.Entity.Order;
import eatclub.rtoms.Services.OrderService;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean   // ✅ New annotation replacing @MockBean
    private OrderService service;

    @Test
    void testPlaceOrder() throws Exception {
        Order order = new Order();
        order.setStatus("PLACED");

        Mockito.when(service.placeOrder(any(OrderRequest.class)))
                .thenReturn(order);

        String json = """
            {
              "customerId": "%s",
              "restaurantId": "%s",
              "items": []
            }
            """.formatted(UUID.randomUUID(), UUID.randomUUID());

        mvc.perform(post("/order/place")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllOrders() throws Exception {
        Mockito.when(service.getAllOrders())
                .thenReturn(List.of(new Order()));

        mvc.perform(get("/order/all"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetOrdersByCustomer() throws Exception {
        Mockito.when(service.getOrdersByCustomer(any(UUID.class)))
                .thenReturn(List.of(new Order()));

        mvc.perform(get("/order/" + UUID.randomUUID()))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateOrderStatus() throws Exception {
        Order order = new Order();
        order.setStatus("PREPARING");

        Mockito.when(service.updateOrderStatus(
                        any(UUID.class),  // orderId
                        anyString(),      // status
                        any(UUID.class))) // restaurantId
                .thenReturn(order);

        mvc.perform(put("/order/" + UUID.randomUUID() + "/status")
                        .param("restaurantId", UUID.randomUUID().toString())
                        .param("status", "PREPARING"))
                .andExpect(status().isOk());
    }

    @Test
    void testCancelOrderByCustomer() throws Exception {
        Order order = new Order();
        order.setStatus("CANCELLED");

        Mockito.when(service.cancelOrderByCustomer(
                        any(UUID.class),  // orderId
                        any(UUID.class))) // customerId
                .thenReturn(order);

        mvc.perform(put("/order/" + UUID.randomUUID() + "/cancel")
                        .param("customerId", UUID.randomUUID().toString()))
                .andExpect(status().isOk());
    }
}
