package com.anna.schedule.employee;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/add")
    public ResponseEntity<Employee> add(@RequestBody Employee employee) {
        return employeeService.add(employee);
    }

    @GetMapping("/get/{phoneNumber}")
    public ResponseEntity get(@PathVariable String phoneNumber) {
        return employeeService.get(phoneNumber);
    }
}
