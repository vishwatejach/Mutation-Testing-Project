package com.example.spebackend.controller;

import com.example.spebackend.model.Order;
import com.example.spebackend.model.OrderItem;
import com.example.spebackend.service.OrderProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderProcessor orderProcessor;

    @Test
    void testCalculateOrderTotal() throws Exception {
        // Create the OrderItem (assuming an OrderItem class exists)
        OrderItem item1 = new OrderItem();
        item1.setProductName("Laptop");
        item1.setQuantity(1);
        item1.setPrice(1000.00);
        item1.setDiscount(0.0);
        item1.setWeight(2.5);
        item1.setFragile(true);

        // Create the Order object
        Order order = new Order();
        order.setCustomerName("Maharshi");
        order.setShippingAddress("ECity");
        order.setStatus("PENDING");
        order.setOrderDate(LocalDateTime.of(2024, 11, 25, 10, 0));
        order.setPaymentMethod("CREDIT_CARD");
        order.setPriority(false);
        order.setRegion("URBAN");
        order.setItems(List.of(item1));

        // Mocking the order processing behavior
        when(orderProcessor.validateOrder(order)).thenReturn("VALID");
        when(orderProcessor.calculateTotalPrice(order)).thenReturn(1000.00);

        // Create the request JSON string with the given JSON structure
        String requestBody = "{\n" +
                "  \"customerName\": \"Maharshi\",\n" +
                "  \"shippingAddress\": \"ECity\",\n" +
                "  \"items\": [\n" +
                "    {\n" +
                "      \"productName\": \"Laptop\",\n" +
                "      \"category\": \"ELECTRONICS\",\n" +
                "      \"price\": 1000.00,\n" +
                "      \"quantity\": 1,\n" +
                "      \"discount\": 0.0,\n" +
                "      \"weight\": 2.5,\n" +
                "      \"fragile\": true\n" +
                "    }\n" +
                "  ],\n" +
                "  \"orderDate\": \"2024-11-25T10:00:00\",\n" +
                "  \"paymentMethod\": \"CREDIT_CARD\",\n" +
                "  \"isPriority\": false,\n" +
                "  \"region\": \"URBAN\"\n" +
                "}";

        // Perform the mock request and assertions
        mockMvc.perform(post("/orders/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())  // Expecting a 200 status
                .andExpect(jsonPath("$.total").value(1000.00));  // Expecting the calculated total price
    }
}
