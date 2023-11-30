package com.anna.schedule.employer;

import com.anna.schedule.order.Order;
import com.anna.schedule.util.DataResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employer")
public class EmployerController {

    private EmployerService employerService;

    public EmployerController(EmployerService employerService) {
        this.employerService = employerService;
    }

    @PostMapping("/add")
    public ResponseEntity<DataResponse<Employer>> add(@RequestBody Employer employer) {
        return new ResponseEntity<>(employerService.add(employer), HttpStatus.OK);
    }

    @GetMapping("/get/{employerId}")
    public ResponseEntity<DataResponse<Employer>> get(@PathVariable("employerId") String employerId) {
        return new ResponseEntity<>(employerService.get(employerId), HttpStatus.OK);
    }

    @GetMapping("/get-all-orders/{employerId}")
    public ResponseEntity<DataResponse<List<Order>>> getAllOrders(@PathVariable("employeeId") String employerId) {
        return new ResponseEntity<>(employerService.getAllOrders(employerId), HttpStatus.OK);
    }
}
