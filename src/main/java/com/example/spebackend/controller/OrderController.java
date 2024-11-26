package com.example.spebackend.controller;

import com.example.spebackend.model.Order;
import com.example.spebackend.service.OrderProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderProcessor orderProcessor;

    @Autowired
    public OrderController(OrderProcessor orderProcessor) {
        this.orderProcessor = orderProcessor;
    }

    @PostMapping("/calculate")
    public ResponseEntity<?> calculateOrderTotal(@RequestBody Order order) {
        try {
            // Validate the order (assuming this method checks if the order is valid)
            String validationResult = orderProcessor.validateOrder(order);
            if (!"VALID".equals(validationResult)) {
                return ResponseEntity.badRequest().body(new ErrorResponse(validationResult));
            }

            // Calculate the total price of the order
            double total = orderProcessor.calculateTotalPrice(order);

            // Return the calculated total and a success message
            return ResponseEntity.ok().body(new OrderResponse(total, "Order total calculated successfully"));

        } catch (IllegalArgumentException e) {
            // Handle invalid arguments and return a bad request response with the error message
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            // Catch any other exceptions and return an internal server error response
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("An error occurred while processing the order"));
        }
    }


    @PostMapping("/validate")
    public ResponseEntity<?> validateOrder(@RequestBody Order order) {
        try {
            String validationResult = orderProcessor.validateOrder(order);
            if ("VALID".equals(validationResult)) {
                return ResponseEntity.ok().body(new ValidationResponse(true, "Order is valid"));
            } else {
                return ResponseEntity.badRequest().body(new ValidationResponse(false, validationResult));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("An error occurred while validating the order"));
        }
    }

    @PostMapping("/shipping-cost")
    public ResponseEntity<?> calculateShippingCost(@RequestParam String region) {
        try {
            double shippingCost = orderProcessor.calculateDistanceCost(region);
            return ResponseEntity.ok().body(new ShippingCostResponse(shippingCost));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("An error occurred while calculating shipping cost"));
        }
    }

    // Response classes for better API structure
    private static class OrderResponse {
        private final double total;
        private final String message;

        public OrderResponse(double total, String message) {
            this.total = total;
            this.message = message;
        }

        public double getTotal() {
            return total;
        }

        public String getMessage() {
            return message;
        }
    }

    private static class ValidationResponse {
        private final boolean valid;
        private final String message;

        public ValidationResponse(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }

        public boolean isValid() {
            return valid;
        }

        public String getMessage() {
            return message;
        }
    }

    private static class ShippingCostResponse {
        private final double cost;

        public ShippingCostResponse(double cost) {
            this.cost = cost;
        }

        public double getCost() {
            return cost;
        }
    }

    private static class ErrorResponse {
        private final String error;

        public ErrorResponse(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }
    }
}