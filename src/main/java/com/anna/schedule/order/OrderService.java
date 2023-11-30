package com.anna.schedule.order;

import com.anna.schedule.employee.Employee;
import com.anna.schedule.employee.EmployeeService;
import com.anna.schedule.employer.Employer;
import com.anna.schedule.employer.EmployerService;
import com.anna.schedule.util.DataResponse;
import com.anna.schedule.util.OrderValidator;
import com.anna.schedule.util.ResponseMessage;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private OrderRepository orderRepository;

    private EmployeeService employeeService;

    private EmployerService employerService;

    public OrderService(OrderRepository orderRepository,
                        EmployeeService employeeService,
                        EmployerService employerService) {
        this.orderRepository = orderRepository;
        this.employeeService = employeeService;
        this.employerService = employerService;
    }

    public DataResponse<Order> add(Order order, String employeeId, String employerId) {
        String orderValidationMessage = OrderValidator.validateDate(order);

        if (orderValidationMessage != null) {
            return new DataResponse<>(order, orderValidationMessage);
        }

        Employee employee = employeeService.get(employeeId).getData();

        if (employee == null) {
            return new DataResponse<>(order, ResponseMessage.EMPLOYEE_IS_NOT_FOUND);
        }

        Employer employer = employerService.get(employerId).getData();

        if (employer == null) {
            return new DataResponse<>(order, ResponseMessage.EMPLOYER_IS_NOT_FOUND);
        }

        order.setEmployee(employee);
        order.setEmployer(employer);

        return new DataResponse<>(orderRepository.save(order), null);
    }

    public DataResponse<Order> get(long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);

        return order == null
                ? new DataResponse<>(null, ResponseMessage.ORDER_IS_NOT_FOUND)
                : new DataResponse<>(order, null);
    }
}
