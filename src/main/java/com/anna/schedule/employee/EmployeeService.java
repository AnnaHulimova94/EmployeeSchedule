package com.anna.schedule.employee;

import com.anna.schedule.order.Order;
import com.anna.schedule.util.DataResponse;
import com.anna.schedule.util.OrderFileWriter;
import com.anna.schedule.util.ResponseMessage;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public DataResponse<Employee> add(Employee employee) {
        if (employeeRepository.getByPhoneNumber(employee.getPhoneNumber()) != null) {
            return new DataResponse<>(null, ResponseMessage.PHONE_NUMBER_ALREADY_IN_USE);
        }

        if (employee.getPhoneNumber() == null
                || !employee.getPhoneNumber().matches("\\+([0-9]{12})")) {
            return new DataResponse<>(null, ResponseMessage.PHONE_NUMBER_FORMAT_IS_INVALID);
        }

        return new DataResponse<>(employeeRepository.save(employee), null);
    }

    public DataResponse<Employee> get(long id) {
        Employee employee = employeeRepository.findById(id).orElse(null);

        return employee == null
                ? new DataResponse<>(null, ResponseMessage.EMPLOYEE_IS_NOT_FOUND)
                : new DataResponse<>(employee, null);
    }

    public DataResponse<List<Employee>> getAll() {
        return new DataResponse<>(employeeRepository.findAll(), null);
    }

    public DataResponse<List<Order>> getAllOrders(long id) {
        Employee employee = employeeRepository.getReferenceById(id);

        return employee == null
                ? new DataResponse<>(null, ResponseMessage.EMPLOYEE_IS_NOT_FOUND)
                : new DataResponse<>(employee.getOrderList(), null);
    }

    public Workbook getScheduleFile(long id) {
        Employee employee = employeeRepository.getReferenceById(id);

        if (employee == null) {
            return null;
        }

        return new OrderFileWriter().generate(employee.getOrderList(), employee);
    }
}
