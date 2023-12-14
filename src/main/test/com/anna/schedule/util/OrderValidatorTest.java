package com.anna.schedule.util;

import com.anna.schedule.order.Order;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class OrderValidatorTest {

    @Test
    @DisplayName("Test validation of order`s dates when valid")
    void test_validation_of_order_dates_when_valid() {
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endTime = LocalDateTime.now().plusDays(2);

        Order order = new Order();

        order.setStartTime(startTime);
        order.setEndTime(endTime);

        String validationMessage = OrderValidator.validateDate(order);

        Assertions.assertNull(validationMessage);
    }

    @Test
    @DisplayName("Test validation of order`s dates when invalid")
    void test_validation_of_order_dates_when_invalid() {
        LocalDateTime startTime = LocalDateTime.now().plusDays(2);
        LocalDateTime endTime = LocalDateTime.now().plusDays(1);

        Order order = new Order();

        order.setStartTime(startTime);
        order.setEndTime(endTime);

        String validationMessage = OrderValidator.validateDate(order);

        Assertions.assertEquals("Date/time is invalid", validationMessage);

        order.setEndTime(startTime);

        Assertions.assertEquals("Date/time is invalid", validationMessage);
    }
}
