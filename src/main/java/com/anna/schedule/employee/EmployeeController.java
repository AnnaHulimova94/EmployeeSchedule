package com.anna.schedule.employee;

import com.anna.schedule.order.Order;
import com.anna.schedule.util.DataResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/add")
    public ResponseEntity<DataResponse<Employee>> add(@RequestBody Employee employee) {
        return new ResponseEntity<>(employeeService.add(employee), HttpStatus.OK);
    }

    @GetMapping("/get/{employeeId}")
    public ResponseEntity<DataResponse<Employee>> get(@PathVariable("employeeId") String employeeId) {
        return new ResponseEntity<>(employeeService.get(employeeId), HttpStatus.OK);
    }

    @GetMapping("/get-all-orders/{employeeId}")
    public ResponseEntity<DataResponse<List<Order>>> getAllOrders(@PathVariable("employeeId") String employeeId) {
        return new ResponseEntity<>(employeeService.getAllOrders(employeeId), HttpStatus.OK);
    }
}
