package com.anna.schedule.order;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderDTO {

    private Order order;

    private long orderId;

    private long employerId;

    private long employeeId;
}
