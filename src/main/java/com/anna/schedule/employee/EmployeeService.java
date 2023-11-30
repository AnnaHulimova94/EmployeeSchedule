package com.anna.schedule.employee;

import com.anna.schedule.order.Order;
import com.anna.schedule.util.DataResponse;
import com.anna.schedule.util.ResponseMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public DataResponse<Employee> add(Employee employee) {
        if (employeeRepository.findById(employee.getId()).isPresent()) {
            return new DataResponse<>(employee, ResponseMessage.EMPLOYEE_ALREADY_EXITS);
        }

        return new DataResponse<>(employeeRepository.save(employee), null);
    }

    public DataResponse<Employee> get(String id) {
        Employee employee = employeeRepository.getByPhoneNumber(id);

        return employee == null
                ? new DataResponse<>(null, ResponseMessage.EMPLOYEE_IS_NOT_FOUND)
                : new DataResponse<>(employee, null);
    }

    public DataResponse<List<Order>> getAllOrders(String id) {
        Employee employee = employeeRepository.getByPhoneNumber(id);

        return employee == null
                ? new DataResponse<>(null, ResponseMessage.EMPLOYEE_IS_NOT_FOUND)
                : new DataResponse<>(employee.getOrderList(), null);
    }
}
