package com.anna.schedule.order;

import com.anna.schedule.employee.Employee;
import com.anna.schedule.employee.EmployeeService;
import com.anna.schedule.employer.Employer;
import com.anna.schedule.employer.EmployerService;
import com.anna.schedule.util.DataResponse;
import com.anna.schedule.util.OrderValidator;
import com.anna.schedule.util.ResponseMessage;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    public DataResponse<Order> create(Order order, long employerId) {
        String orderValidationMessage = OrderValidator.validateDate(order);

        if (orderValidationMessage != null) {
            return new DataResponse<>(order, orderValidationMessage);
        }

        Employer employer = employerService.get(employerId).getData();

        if (employer == null) {
            return new DataResponse<>(order, ResponseMessage.EMPLOYER_IS_NOT_FOUND);
        }
        order.setEmployer(employer);

        return new DataResponse<>(orderRepository.save(order), null);
    }

    public DataResponse<Order> addEmployee(long orderId, long employeeId) {
        Order order = orderRepository.findById(orderId).orElse(null);

        if (order == null) {
            return new DataResponse<>(null, ResponseMessage.ORDER_IS_NOT_FOUND);
        }

        Employee employee = employeeService.get(employeeId).getData();

        if (employee == null) {
            return new DataResponse<>(null, ResponseMessage.EMPLOYEE_IS_NOT_FOUND);
        }

        for (Order employeeOrder : employee.getOrderList()) {
            if (isJointDateTime(employeeOrder, order)) {
                return new DataResponse<>(null, ResponseMessage.UNSUITABLE_DATE_TIME);
            }
        }

        order.setEmployee(employee);

        return new DataResponse<>(orderRepository.save(order), null);
    }

    public DataResponse<List<Order>> getSuitableOrderList(long employeeId) {
        Employee employee = employeeService.get(employeeId).getData();

        if (employee == null) {
            return new DataResponse<>(null, ResponseMessage.EMPLOYEE_IS_NOT_FOUND);
        }

        return new DataResponse<>(getDisjointOrderList(employee.getOrderList(),
                orderRepository.getFreeOrderList()), null);
    }

    public List<Order> getDisjointOrderList(List<Order> employeeOrderList, List<Order> orderList) {
        Set<Order> unsuitableOrderList = new HashSet<>();

        for (Order employeeOrder : employeeOrderList) {
            unsuitableOrderList.addAll(orderList.stream()
                    .filter(order -> isJointDateTime(employeeOrder, order))
                    .collect(Collectors.toSet()));
        }

        orderList.removeAll(unsuitableOrderList);

        return orderList;
    }

    public DataResponse<Order> get(long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);

        return order == null
                ? new DataResponse<>(null, ResponseMessage.ORDER_IS_NOT_FOUND)
                : new DataResponse<>(order, null);
    }

    public DataResponse<Order> delete(long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);

        if (order == null) {
            return new DataResponse<>(null, ResponseMessage.ORDER_IS_NOT_FOUND);
        }

        orderRepository.delete(order);

        return new DataResponse<>(order, null);
    }

    public DataResponse<Order> deleteEmployee(long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);

        if (order == null) {
            return new DataResponse<>(null, ResponseMessage.ORDER_IS_NOT_FOUND);
        }

        order.setEmployee(null);

        return new DataResponse<>(orderRepository.save(order), null);
    }

    private boolean isJointDateTime(Order employeeOrder, Order order) {
        return (employeeOrder.getEndTime().isAfter(order.getStartTime())
                && employeeOrder.getEndTime().isBefore(order.getEndTime()))

                ||
                (employeeOrder.getStartTime().isAfter(order.getStartTime())
                        && employeeOrder.getStartTime().isBefore(order.getEndTime()))

                ||
                (employeeOrder.getStartTime().isBefore(order.getStartTime())
                        && employeeOrder.getEndTime().isAfter(order.getEndTime()))

                || employeeOrder.getStartTime().isEqual(order.getStartTime())
                || employeeOrder.getStartTime().isEqual(order.getEndTime())
                || employeeOrder.getEndTime().isEqual(order.getStartTime())
                || employeeOrder.getEndTime().isEqual(order.getEndTime());
    }
}
