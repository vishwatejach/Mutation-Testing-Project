package com.example.spebackend.controller;


import com.example.spebackend.controller.OrderController;
import com.example.spebackend.model.Order;
import com.example.spebackend.model.OrderItem;
import com.example.spebackend.service.OrderProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OrderControllerIntegrationTest {

    @Autowired
    private OrderController orderController;

    @Autowired
    private OrderProcessor orderProcessor;

    private Order validOrder;

    @BeforeEach
    public void setUp() {
        // Create a valid order for testing
        validOrder = new Order();
        validOrder.setCustomerName("John Doe");
        validOrder.setShippingAddress("123 Test Street");
        validOrder.setOrderDate(LocalDateTime.now());
        validOrder.setPaymentMethod("CREDIT_CARD");
        validOrder.setRegion("URBAN");
        validOrder.setPriority(false);

        // Create order items
        List<OrderItem> items = new ArrayList<>();
        OrderItem item1 = new OrderItem();
        item1.setProductName("Laptop");
        item1.setPrice(1000.0);
        item1.setQuantity(1);
        item1.setCategory("ELECTRONICS");
        item1.setDiscount(0.0);
        item1.setWeight(2.5);
        item1.setFragile(false);

        items.add(item1);
        validOrder.setItems(items);
    }

    @Test
    public void testValidOrderCalculation() {
        // Test order total calculation
        ResponseEntity<?> response = orderController.calculateOrderTotal(validOrder);

        assertTrue(response.getStatusCode().is2xxSuccessful());

        // Verify the response is an OrderResponse
        assertTrue(response.getBody() instanceof OrderController.OrderResponse);

        OrderController.OrderResponse orderResponse = (OrderController.OrderResponse) response.getBody();

        // Verify the calculated total matches the manual calculation
        double expectedTotal = orderProcessor.calculateTotalPrice(validOrder);
        assertEquals(expectedTotal, orderResponse.getTotal(), 0.01);
    }

    @Test
    public void testOrderValidation() {
        // Test order validation
        ResponseEntity<?> response = orderController.validateOrder(validOrder);

        assertTrue(response.getStatusCode().is2xxSuccessful());

        // Verify the response is a ValidationResponse
        assertTrue(response.getBody() instanceof OrderController.ValidationResponse);

        OrderController.ValidationResponse validationResponse = (OrderController.ValidationResponse) response.getBody();

        assertTrue(validationResponse.isValid());
        assertEquals("Order is valid", validationResponse.getMessage());
    }

    @Test
    public void testInvalidOrderCalculation() {
        // Create an invalid order with missing required fields
        Order invalidOrder = new Order();

        ResponseEntity<?> response = orderController.calculateOrderTotal(invalidOrder);

        assertTrue(response.getStatusCode().is4xxClientError());

        // Verify the response is an ErrorResponse
        assertTrue(response.getBody() instanceof OrderController.ErrorResponse);

        OrderController.ErrorResponse errorResponse = (OrderController.ErrorResponse) response.getBody();

        assertEquals("Invalid order: Customer name is required", errorResponse.getError());
    }

    @Test
    public void testShippingCostCalculation() {
        // Test shipping cost calculation for different regions
        String[] regions = {"URBAN", "SUBURBAN", "RURAL"};

        for (String region : regions) {
            ResponseEntity<?> response = orderController.calculateShippingCost(region);

            assertTrue(response.getStatusCode().is2xxSuccessful());

            // Verify the response is a ShippingCostResponse
            assertTrue(response.getBody() instanceof OrderController.ShippingCostResponse);

            OrderController.ShippingCostResponse shippingResponse = (OrderController.ShippingCostResponse) response.getBody();

            // Verify the shipping cost matches the calculation in OrderProcessor
            double expectedCost = orderProcessor.calculateDistanceCost(region);
            assertEquals(expectedCost, shippingResponse.getCost(), 0.01);
        }
    }

    @Test
    public void testWeekendDiscountScenario() {
        // Create an order on a weekend (Saturday)
        validOrder.setOrderDate(LocalDateTime.now().withDayOfMonth(20).withMonth(7)); // Ensure it's a Saturday

        ResponseEntity<?> response = orderController.calculateOrderTotal(validOrder);

        assertTrue(response.getStatusCode().is2xxSuccessful());

        OrderController.OrderResponse orderResponse = (OrderController.OrderResponse) response.getBody();

        // Verify the total reflects the weekend discount
        double expectedTotal = orderProcessor.calculateTotalPrice(validOrder);
        assertEquals(expectedTotal, orderResponse.getTotal(), 0.01);
    }

    @Test
    public void testOrderControllerConstructor() {
        // Verify that the OrderController is created with a non-null OrderProcessor
        assertNotNull(orderController);

        // Use reflection to access the private orderProcessor field
        try {
            Field processorField = OrderController.class.getDeclaredField("orderProcessor");
            processorField.setAccessible(true);
            OrderProcessor extractedProcessor = (OrderProcessor) processorField.get(orderController);

            assertNotNull(extractedProcessor);
            assertEquals(orderProcessor, extractedProcessor);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Unable to verify OrderProcessor injection: " + e.getMessage());
        }
    }

    @Test
    public void testOrderCalculationErrorScenarios() {
        // Test scenario that triggers IllegalArgumentException
        Order problematicOrder = new Order();
        problematicOrder.setCustomerName("Test User");
        problematicOrder.setShippingAddress("Test Address");
        problematicOrder.setOrderDate(LocalDateTime.now());
        problematicOrder.setPaymentMethod("CREDIT_CARD");

        // Create an empty items list to trigger IllegalArgumentException
        problematicOrder.setItems(new ArrayList<>());

        try {
            ResponseEntity<?> response = orderController.calculateOrderTotal(problematicOrder);

            // Verify error response
            assertNotNull(response);
            assertTrue(response.getStatusCode().is4xxClientError());

            // Verify ErrorResponse is created with a message
            assertTrue(response.getBody() instanceof OrderController.ErrorResponse);
            OrderController.ErrorResponse errorResponse = (OrderController.ErrorResponse) response.getBody();
            assertNotNull(errorResponse.getError());
            assertFalse(errorResponse.getError().isEmpty());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testErrorResponseConstructor() {
        String errorMessage = "Test Error Message";
        OrderController.ErrorResponse errorResponse = new OrderController.ErrorResponse(errorMessage);

        // Verify constructor works correctly
        assertNotNull(errorResponse);
        assertEquals(errorMessage, errorResponse.getError());
    }




}