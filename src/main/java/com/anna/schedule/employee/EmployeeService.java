package com.anna.schedule.employee;

import com.anna.schedule.order.Order;
import com.anna.schedule.util.DataResponse;
import com.anna.schedule.util.OrderFileWriter;
import com.anna.schedule.util.ResponseMessage;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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

    public DataResponse<Employee> get(String phoneNumber) {
        Employee employee = employeeRepository.getByPhoneNumber(phoneNumber);

        return employee == null
                ? new DataResponse<>(null, ResponseMessage.EMPLOYEE_IS_NOT_FOUND)
                : new DataResponse<>(employee, null);
    }

    public DataResponse<Employee> get(long id) {
        Employee employee = employeeRepository.findById(id).orElse(null);

        return employee == null
                ? new DataResponse<>(null, ResponseMessage.EMPLOYEE_IS_NOT_FOUND)
                : new DataResponse<>(employee, null);
    }

    public DataResponse<List<Order>> getAllOrders(long id) {
        Employee employee = employeeRepository.findById(id).orElse(null);

        return employee == null
                ? new DataResponse<>(null, ResponseMessage.EMPLOYEE_IS_NOT_FOUND)
                : new DataResponse<>(employee.getOrderList(), null);
    }

    public Workbook getScheduleFile(long id) {
        Employee employee = employeeRepository.findById(id).orElse(null);

        if (employee == null) {
            return null;
        }

        return new OrderFileWriter().generate(employee.getOrderList(), employee);
    }
}
