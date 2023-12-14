package com.anna.schedule.order;

import com.anna.schedule.employee.Employee;
import com.anna.schedule.employee.EmployeeService;
import com.anna.schedule.employer.Employer;
import com.anna.schedule.employer.EmployerService;
import com.anna.schedule.util.DataResponse;
import com.anna.schedule.util.ResponseMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private EmployerService employerService;

    @Mock
    private EmployeeService employeeService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("Test creation of the order when employer is found and order is valid")
    @Test
    void test_create_order_when_order_is_valid() {
        Order order = new Order();
        order.setStartTime(LocalDateTime.now().plusDays(1));
        order.setEndTime(LocalDateTime.now().plusDays(2));

        Employer employer = new Employer();
        employer.setId(1);

        doReturn(new DataResponse<>(employer, null)).when(employerService).get(anyLong());
        doReturn(order).when(orderRepository).save(order);

        DataResponse dataResponse = orderService.create(order, employer.getId());

        Assertions.assertNull(dataResponse.getMessage());

        verify(employerService).get(anyLong());
        verify(orderRepository).save(order);
    }

    @DisplayName("Test creation of the order when order is invalid")
    @Test
    void test_create_order_when_order_is_invalid() {
        Order order = new Order();
        order.setStartTime(LocalDateTime.now().plusDays(2));
        order.setEndTime(LocalDateTime.now().plusDays(1));

        DataResponse dataResponse = orderService.create(order, 1);

        Assertions.assertNull(dataResponse.getData());
        Assertions.assertEquals("Date/time is invalid", dataResponse.getMessage());

        verifyNoInteractions(employeeService);
        verifyNoInteractions(orderRepository);
    }

    @DisplayName("Test creation of the order when order is valid but employer is not found")
    @Test
    void test_create_order_when_employer_is_not_found() {
        Order order = new Order();
        order.setStartTime(LocalDateTime.now().plusDays(1));
        order.setEndTime(LocalDateTime.now().plusDays(2));

        doReturn(new DataResponse<>(null, ResponseMessage.EMPLOYER_IS_NOT_FOUND))
                .when(employerService)
                .get(anyLong());

        DataResponse dataResponse = orderService.create(order, 1);

        Assertions.assertNull(dataResponse.getData());
        Assertions.assertEquals(ResponseMessage.EMPLOYER_IS_NOT_FOUND, dataResponse.getMessage());

        verify(employerService).get(anyLong());
        verifyNoInteractions(orderRepository);
    }

    @DisplayName("Test adding employee to order when both are found")
    @Test
    void test_add_employee_to_order_when_order_and_employee_are_found() {
        Order order = new Order();

        doReturn(Optional.of(order)).when(orderRepository).findById(anyLong());
        doReturn(new DataResponse<>(new Employee(), null)).when(employeeService).get(anyLong());
        doReturn(order).when(orderRepository).save(order);

        DataResponse dataResponse = orderService.addEmployee(1, 1);

        Assertions.assertNull(dataResponse.getMessage());

        verify(employeeService).get(anyLong());
        verify(orderRepository).findById(anyLong());
        verify(orderRepository).save(order);
    }

    @DisplayName("Test adding employee to order when order is not found")
    @Test
    void test_add_employee_to_order_when_order_is_not_found() {
        doReturn(null).when(orderRepository).getReferenceById(anyLong());

        DataResponse dataResponse = orderService.addEmployee(1, 1);

        Assertions.assertNull(dataResponse.getData());
        Assertions.assertEquals(ResponseMessage.ORDER_IS_NOT_FOUND, dataResponse.getMessage());

        verify(orderRepository).findById(anyLong());
        verifyNoInteractions(employeeService);
    }

    @DisplayName("Test adding employee to order when employee is not found")
    @Test
    void test_add_employee_to_order_when_employee_is_not_found() {
        doReturn(Optional.of(new Order())).when(orderRepository).findById(anyLong());
        doReturn(new DataResponse<>(null, ResponseMessage.EMPLOYEE_IS_NOT_FOUND))
                .when(employeeService)
                .get(anyLong());

        DataResponse dataResponse = orderService.addEmployee(1, 1);

        Assertions.assertNull(dataResponse.getData());
        Assertions.assertEquals(ResponseMessage.EMPLOYEE_IS_NOT_FOUND, dataResponse.getMessage());

        verify(orderRepository).findById(anyLong());
        verify(employeeService).get(anyLong());
    }

    @DisplayName("Test adding employee to order when order is already assigned")
    @Test
    void test_add_employee_to_order_is_already_assigned(){
        Order order = new Order();
        order.setEmployee(new Employee());

        doReturn(Optional.of(order)).when(orderRepository).findById(anyLong());

        DataResponse dataResponse = orderService.addEmployee(1, 1);

        Assertions.assertNull(dataResponse.getData());
        Assertions.assertEquals(ResponseMessage.ORDER_IS_ASSIGNED, dataResponse.getMessage());

        verify(orderRepository).findById(anyLong());
        verifyNoInteractions(employeeService);
    }

    @DisplayName("Test getting suitable order list for employee when employee is found")
    @Test
    void test_get_suitable_order_list_for_employee_when_employee_is_found() {
        Employee employee = new Employee();

        int year = LocalDateTime.now().getYear();
        int month = LocalDateTime.now().getMonth().getValue();

        if (month == 12) {
            month = 1;
            year++;
        } else {
            month++;
        }

        Order order1 = new Order();
        order1.setStartTime(LocalDateTime.of(year, month, 20, 12, 0));
        order1.setEndTime(LocalDateTime.of(year, month, 20, 15, 0));

        Order order2 = new Order();
        order2.setStartTime(LocalDateTime.of(year, month, 20, 11, 0));
        order2.setEndTime(LocalDateTime.of(year, month, 20, 16, 0));

        Order order3 = new Order();
        order3.setStartTime(LocalDateTime.of(year, month, 20, 14, 45));
        order3.setEndTime(LocalDateTime.of(year, month, 20, 17, 0));

        Order order4 = new Order();
        order4.setStartTime(LocalDateTime.of(year, month, 20, 13, 0));
        order4.setEndTime(LocalDateTime.of(year, month, 20, 14, 0));

        Order order5 = new Order();
        order5.setStartTime(LocalDateTime.of(year, month, 20, 11, 0));
        order5.setEndTime(LocalDateTime.of(year, month, 20, 14, 0));

        Order order6 = new Order();
        order6.setStartTime(LocalDateTime.of(year, month, 20, 10, 0));
        order6.setEndTime(LocalDateTime.of(year, month, 20, 12, 0));

        Order order7 = new Order();
        order7.setStartTime(LocalDateTime.of(year, month, 20, 15, 0));
        order7.setEndTime(LocalDateTime.of(year, month, 20, 17, 0));

        Order order8 = new Order();
        order8.setStartTime(LocalDateTime.of(year, month, 20, 19, 0));
        order8.setEndTime(LocalDateTime.of(year, month, 20, 20, 0));

        Order order9 = new Order();
        order9.setStartTime(LocalDateTime.of(year, month, 20, 9, 0));
        order9.setEndTime(LocalDateTime.of(year, month, 20, 10, 0));

        List<Order> orderList = new ArrayList<>();
        orderList.add(order1);
        orderList.add(order2);
        orderList.add(order3);
        orderList.add(order4);
        orderList.add(order5);
        orderList.add(order6);
        orderList.add(order7);
        orderList.add(order8);
        orderList.add(order9);

        doReturn(new DataResponse<>(employee, null)).when(employeeService).get(anyLong());
        doReturn(orderList).when(orderRepository).getFreeOrderList();

        //employee`s order list is empty now, so all orders should be suitable
        List<Order> suitableOrders = orderService.getSuitableOrderList(1).getData();

        Assertions.assertEquals(9, suitableOrders.size());

        for (Order order : orderList) {
            Assertions.assertTrue(suitableOrders.contains(order));
        }

        //added first order, only 8s and 9s order should be suitable now
        employee.getOrderList().add(order1);
        orderList.remove(order1);

        suitableOrders = orderService.getSuitableOrderList(1).getData();

        Assertions.assertEquals(2, suitableOrders.size());
        Assertions.assertTrue(suitableOrders.contains(order8));
        Assertions.assertTrue(suitableOrders.contains(order9));

        verify(employeeService, times(2)).get(anyLong());
        verify(orderRepository, times(2)).getFreeOrderList();
    }

    @DisplayName("Test getting suitable order list for employee when employee is not found")
    @Test
    void test_get_suitable_order_list_for_employee_when_employee_is_not_found() {
        doReturn(new DataResponse<>(null, ResponseMessage.EMPLOYEE_IS_NOT_FOUND))
                .when(employeeService)
                .get(anyLong());

        DataResponse dataResponse = orderService.getSuitableOrderList(1);

        Assertions.assertNull(dataResponse.getData());
        Assertions.assertEquals(ResponseMessage.EMPLOYEE_IS_NOT_FOUND, dataResponse.getMessage());

        verify(employeeService).get(anyLong());
        verifyNoInteractions(orderRepository);
    }
}
