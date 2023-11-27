package com.anna.schedule.employee;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    private EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public ResponseEntity<Employee> add(Employee employee) {
        try {
            employee = employeeRepository.save(employee);

            return new ResponseEntity<>(employee, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(employee, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Employee> get(String phoneNumber) {
        Employee employee = employeeRepository.getByPhoneNumber(phoneNumber);

        if (employee == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(employee, HttpStatus.OK);
    }
}
