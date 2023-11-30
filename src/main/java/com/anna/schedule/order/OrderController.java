package com.anna.schedule.order;

import com.anna.schedule.util.DataResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/add/{employeeId}/{employerId}")
    public ResponseEntity<DataResponse<Order>> add(@RequestBody Order order,
                                            @PathVariable("employeeId") String employeeId,
                                            @PathVariable("employerId") String employerId){
        return new ResponseEntity<>(orderService.add(order, employeeId, employerId), HttpStatus.OK);
    }

    @GetMapping("/get/{orderId}")
    public ResponseEntity<DataResponse<Order>> get(@PathVariable("orderId") long orderId){
        return new ResponseEntity<>(orderService.get(orderId), HttpStatus.OK);
    }
}
