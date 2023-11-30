package com.anna.schedule.util;

import com.anna.schedule.order.Order;

import java.time.LocalDateTime;

public class OrderValidator {

    public static String validateDate(Order order) {
        return order.getStartTime().isBefore(LocalDateTime.now())
                || order.getStartTime().isBefore(order.getEndTime()) ? null
                : "Date/time is invalid";
    }
}
