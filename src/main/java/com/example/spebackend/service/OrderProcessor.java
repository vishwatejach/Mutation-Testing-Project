package com.example.spebackend.service;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.example.spebackend.model.Order;
import com.example.spebackend.model.OrderItem;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.DayOfWeek;
import java.util.List;

@Service
public class OrderProcessor {

    public double calculateTotalPrice(Order order) {
        if (order == null || order.getItems() == null || order.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order cannot be null or empty");
        }

        double total = 0;
        for (OrderItem item : order.getItems()) {
            double itemPrice = calculateItemPrice(item);
            total += itemPrice;
        }


        // Apply order-level discounts
        total = applyOrderLevelDiscounts(total, order);

        // Add shipping cost
        total += calculateShippingCost(order);

        return Math.round(total * 100.0) / 100.0;
    }

    public double calculateItemPrice(OrderItem item) {
        if (item == null) {
            return 0;
        }

        double price = item.getPrice() * item.getQuantity();

        // Apply category-based discounts
        if ("ELECTRONICS".equalsIgnoreCase(item.getCategory())) {
            price *= 0.95; // 5% discount
        }
        if ("BOOKS".equalsIgnoreCase(item.getCategory())) {
            price *= 0.90; // 10% discount
        }

        // Apply quantity-based discounts
//        if (item.getQuantity() >= 10) {
//            price *= 0.93; // 7% bulk discount
//        }
//        else if (item.getQuantity() >= 5) {
//            price *= 0.95; // 5% bulk discount
//        }

        switch (item.getQuantity()) {
            case 10:
                price *= 0.93;
                break;
            case 5:
                price *= 0.95;
                break;
            default:
                price = price;
                break;
        }

        // Apply item-specific discount
        price *= (1 - item.getDiscount());

        return price;
    }

    public double applyOrderLevelDiscounts(double total, Order order) {
        // Weekend discount
        if (isWeekend(order.getOrderDate())) {
            total *= 0.95; // 5% weekend discount
        }

        // Priority customer discount
        if (order.isPriority()) {
            total *= 0.98; // 2% priority discount
        }

        // Large order discount
        if (total >= 8835) {
            total *= 0.95; // 5% large order discount
        }

        // Regional discounts
        if ("RURAL".equalsIgnoreCase(order.getRegion())) {
            total *= 0.97; // 3% rural discount
        }

        return total;
    }

    private double calculateShippingCost(Order order) {
        double baseShippingCost = 10.0;

        // Calculate weight-based cost
        double totalWeight = calculateTotalWeight(order.getItems());
        double weightCost = totalWeight * 0.5;

        // Add fragile item handling cost
        double fragileCost = calculateFragileCost(order.getItems());

        // Add perishable item handling cost
//        double perishableCost = calculatePerishableCost(order.getItems());

        // Calculate distance-based cost
        double distanceCost = calculateDistanceCost(order.getRegion());

        // Priority shipping
        double priorityCost = order.isPriority() ? 15.0 : 0.0;

        return baseShippingCost + weightCost + fragileCost + + distanceCost + priorityCost;
    }

    private double calculateTotalWeight(List<OrderItem> items) {
        return items.stream()
                .mapToDouble(item -> item.getWeight() * item.getQuantity())
                .sum();
    }

    private double calculateFragileCost(List<OrderItem> items) {
        return items.stream()
                .filter(OrderItem::isFragile)
                .mapToDouble(item -> 5.0 * item.getQuantity())
                .sum();
    }

//    private double calculatePerishableCost(List<OrderItem> items) {
//        return items.stream()
//                .filter(OrderItem::isPerishable)
//                .mapToDouble(item -> 3.0 * item.getQuantity())
//                .sum();
//    }

    public double calculateDistanceCost(String region) {
        switch (region.toUpperCase()) {
            case "URBAN":
                return 5.0;
            case "SUBURBAN":
                return 8.0;
            case "RURAL":
                return 12.0;
            default:
                return 10.0;
        }
    }

    private boolean isWeekend(LocalDateTime dateTime) {
        DayOfWeek day = dateTime.getDayOfWeek();
        return day == DayOfWeek.SATURDAY;
    }

    public String validateOrder(Order order) {
        if (order == null) {
            return "Invalid order: Order is null";
        }

        if (order.getCustomerName() == null) {
            return "Invalid order: Customer name is required";
        }

        if (order.getShippingAddress() == null) {
            return "Invalid order: Shipping address is required";
        }

        if (order.getItems() == null || order.getItems().isEmpty()) {
            return "Invalid order: Order must contain items";
        }

        for (OrderItem item : order.getItems()) {
            String itemValidation = validateOrderItem(item);
            if (itemValidation != null) {
                return itemValidation;
            }
        }

        if (order.getOrderDate() == null) {
            return "Invalid order: Order date is required";
        }

        if (order.getPaymentMethod() == null) {
            return "Invalid order: Payment method is required";
        }

        return "VALID";
    }

    public String validateOrderItem(OrderItem item) {
        if (item.getProductName() == null) {
            return "Invalid order item: Product name is required";
        }

        if (item.getPrice() <= 0) {  // Changed from 0.01 to 0
            return "Invalid order item: Price must be greater than 0";
        }


        if (item.getQuantity() <= 0) {
            return "Invalid order item: Quantity must be greater than 0";
        }

        if (item.getDiscount() < 0 || item.getDiscount() >= 1) {
            return "Invalid order item: Discount must be between 0 and 1";
        }

        if (item.getWeight() <= 0) {
            return "Invalid order item: Weight must be greater than 0";
        }

        return null;
    }
}