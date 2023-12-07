package com.anna.schedule.order;

import com.anna.schedule.util.DataResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create/{employerId}")
    public ResponseEntity<DataResponse<Order>> create(@RequestBody Order order,
                                                      @PathVariable("employerId") long employerId) {
        return new ResponseEntity<>(orderService.create(order, employerId), HttpStatus.OK);
    }

    @PostMapping("/add-employee/{orderId}/{employeeId}")
    public ResponseEntity<DataResponse<Order>> addEmployee(@PathVariable("orderId") long orderId,
                                                           @PathVariable("employeeId") long employeeId) {
        return new ResponseEntity<>(orderService.addEmployee(orderId, employeeId), HttpStatus.OK);
    }

    @GetMapping("/get/{orderId}")
    public ResponseEntity<DataResponse<Order>> get(@PathVariable("orderId") long orderId) {
        return new ResponseEntity<>(orderService.get(orderId), HttpStatus.OK);
    }

    @GetMapping("/get-suitable-order-list/{employeeId}")
    public ResponseEntity<DataResponse<List<Order>>> getSuitableOrderList(@PathVariable("employeeId") long employeeId) {
        return new ResponseEntity<>(orderService.getSuitableOrderList(employeeId), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<DataResponse<Order>> delete(@PathVariable("orderId") long orderId) {
        return new ResponseEntity<>(orderService.delete(orderId), HttpStatus.OK);
    }

    @DeleteMapping("delete-employee/{orderId}")
    public ResponseEntity<DataResponse<Order>> deleteEmployee(@PathVariable("orderId") long orderId) {
        return new ResponseEntity<>(orderService.deleteEmployee(orderId), HttpStatus.OK);
    }
}
