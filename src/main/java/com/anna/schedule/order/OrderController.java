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

    @PostMapping("/create")
    public ResponseEntity<DataResponse<Order>> create(@RequestBody OrderDTO orderDTO) {
        return new ResponseEntity<>(orderService.create(orderDTO.getOrder(), orderDTO.getEmployerId()), HttpStatus.OK);
    }

    @PostMapping("/add-employee")
    public ResponseEntity<DataResponse<Order>> addEmployee(@RequestBody OrderDTO orderDTO) {
        return new ResponseEntity<>(orderService.addEmployee(orderDTO.getOrderId(), orderDTO.getEmployeeId()), HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<DataResponse<Order>> get(@PathVariable("orderId") long orderId) {
        return new ResponseEntity<>(orderService.get(orderId), HttpStatus.OK);
    }

    @GetMapping("/{employeeId}/get-suitable-order-list")
    public ResponseEntity<DataResponse<List<Order>>> getSuitableOrderList(@PathVariable("employeeId") long employeeId) {
        return new ResponseEntity<>(orderService.getSuitableOrderList(employeeId), HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<DataResponse<Order>> delete(@PathVariable("orderId") long orderId) {
        return new ResponseEntity<>(orderService.delete(orderId), HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}/delete-employee")
    public ResponseEntity<DataResponse<Order>> deleteEmployee(@PathVariable("orderId") long orderId) {
        return new ResponseEntity<>(orderService.deleteEmployee(orderId), HttpStatus.OK);
    }
}
