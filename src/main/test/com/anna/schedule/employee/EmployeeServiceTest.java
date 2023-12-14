package com.anna.schedule.employee;

import com.anna.schedule.order.Order;
import com.anna.schedule.util.DataResponse;
import com.anna.schedule.util.ResponseMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("Test adding employee with unique phone number")
    @Test
    void test_add_when_phone_is_unique_and_valid() {
        Employee employee = new Employee();
        employee.setPhoneNumber("+354352637485");

        doReturn(null).when(employeeRepository).getByPhoneNumber(anyString());
        doReturn(employee).when(employeeRepository).save(employee);

        DataResponse<Employee> dataResponse = employeeService.add(employee);

        Assertions.assertEquals(employee, dataResponse.getData());
        Assertions.assertNull(dataResponse.getMessage());

        verify(employeeRepository).getByPhoneNumber(anyString());
        verify(employeeRepository).save(employee);
    }

    @DisplayName("Test adding employee with existing phone number")
    @Test
    void test_add_when_phone_is_not_unique() {
        Employee employee = new Employee();
        employee.setPhoneNumber("+387624364787");

        doReturn(employee).when(employeeRepository).getByPhoneNumber(anyString());

        DataResponse<Employee> dataResponse = employeeService.add(employee);

        Assertions.assertNull(dataResponse.getData());
        Assertions.assertEquals(ResponseMessage.PHONE_NUMBER_ALREADY_IN_USE, dataResponse.getMessage());

        verify(employeeRepository).getByPhoneNumber(anyString());
    }

    @DisplayName("Test adding employee with invalid phone number")
    @Test
    void test_add_when_phone_number_format_is_invalid() {
        Employee employee = new Employee();
        employee.setPhoneNumber("3624364787");

        doReturn(null).when(employeeRepository).getByPhoneNumber(anyString());

        DataResponse<Employee> dataResponse = employeeService.add(employee);

        Assertions.assertNull(dataResponse.getData());
        Assertions.assertEquals(ResponseMessage.PHONE_NUMBER_FORMAT_IS_INVALID, dataResponse.getMessage());

        verify(employeeRepository).getByPhoneNumber(anyString());
    }

    @DisplayName("Test getting employee when exists")
    @Test
    void test_get_when_employee_exists() {
        Employee employee = new Employee();
        employee.setId(1);

        doReturn(Optional.of(employee)).when(employeeRepository).findById(anyLong());

        DataResponse<Employee> dataResponse = employeeService.get(employee.getId());

        Assertions.assertEquals(dataResponse.getData(), employee);
        Assertions.assertNull(dataResponse.getMessage());

        verify(employeeRepository).findById(anyLong());
    }

    @DisplayName("Test getting when employee is not found")
    @Test
    void test_get_when_employee_is_not_found() {
        doReturn(Optional.empty()).when(employeeRepository).findById(anyLong());

        DataResponse<Employee> dataResponse = employeeService.get(1);

        Assertions.assertNull(dataResponse.getData());
        Assertions.assertEquals(ResponseMessage.EMPLOYEE_IS_NOT_FOUND, dataResponse.getMessage());

        verify(employeeRepository).findById(anyLong());
    }

    @DisplayName("Test getting all employee`s orders")
    @Test
    void test_get_all_employee_orders() {
        Employee employee = new Employee();
        employee.setId(1);

        Order order1 = new Order();
        Order order2 = new Order();

        employee.getOrderList().add(order1);
        employee.getOrderList().add(order2);

        doReturn(employee).when(employeeRepository).getReferenceById(anyLong());

        DataResponse<List<Order>> dataResponse = employeeService.getAllOrders(employee.getId());

        Assertions.assertEquals(employee.getOrderList(), dataResponse.getData());
        Assertions.assertNull(dataResponse.getMessage());

        verify(employeeRepository).getReferenceById(employee.getId());
    }

    @DisplayName("Test getting all employee`s orders when employee is not found")
    @Test
    void test_get_all_employee_orders_when_employee_is_not_found() {
        doReturn(null).when(employeeRepository).getReferenceById(anyLong());

        DataResponse<List<Order>> dataResponse = employeeService.getAllOrders(1);

        Assertions.assertNull(dataResponse.getData());
        Assertions.assertEquals(ResponseMessage.EMPLOYEE_IS_NOT_FOUND, dataResponse.getMessage());
    }
}
