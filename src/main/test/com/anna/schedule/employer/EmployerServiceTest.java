package com.anna.schedule.employer;

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

public class EmployerServiceTest {

    @InjectMocks
    private EmployerService employerService;

    @Mock
    private EmployerRepository employerRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("Test adding employer with unique and valid phone number")
    @Test
    void test_add_when_phone_is_unique() {
        Employer employer = new Employer();
        employer.setPhoneNumber("+354263654162");

        doReturn(null).when(employerRepository).getByPhoneNumber(anyString());
        doReturn(employer).when(employerRepository).save(employer);

        DataResponse<Employer> dataResponse = employerService.add(employer);

        Assertions.assertEquals(employer, dataResponse.getData());
        Assertions.assertNull(dataResponse.getMessage());

        verify(employerRepository).getByPhoneNumber(anyString());
        verify(employerRepository).save(employer);
    }

    @DisplayName("Test adding employer with existing phone number")
    @Test
    void test_add_when_phone_is_not_unique() {
        Employer employer = new Employer();
        employer.setPhoneNumber("+465352637485");

        doReturn(employer).when(employerRepository).getByPhoneNumber(anyString());

        DataResponse<Employer> dataResponse = employerService.add(employer);

        Assertions.assertNull(dataResponse.getData());
        Assertions.assertEquals(ResponseMessage.PHONE_NUMBER_ALREADY_IN_USE, dataResponse.getMessage());

        verify(employerRepository).getByPhoneNumber(anyString());
    }

    @DisplayName("Test adding employer with invalid phone number")
    @Test
    void test_add_when_phone_number_format_is_invalid() {
        Employer employer = new Employer();
        employer.setPhoneNumber("3624364787");

        doReturn(null).when(employerRepository).getByPhoneNumber(anyString());

        DataResponse<Employer> dataResponse = employerService.add(employer);

        Assertions.assertNull(dataResponse.getData());
        Assertions.assertEquals(ResponseMessage.PHONE_NUMBER_FORMAT_IS_INVALID, dataResponse.getMessage());

        verify(employerRepository).getByPhoneNumber(anyString());
    }

    @DisplayName("Test getting employer when exists")
    @Test
    void test_get_when_employer_exists() {
        Employer employer = new Employer();
        employer.setId(1);

        doReturn(Optional.of(employer)).when(employerRepository).findById(anyLong());

        DataResponse<Employer> dataResponse = employerService.get(employer.getId());

        Assertions.assertEquals(dataResponse.getData(), employer);
        Assertions.assertNull(dataResponse.getMessage());

        verify(employerRepository).findById(anyLong());
    }

    @DisplayName("Test getting when employer is not found")
    @Test
    void test_get_when_employer_is_not_found() {
        doReturn(Optional.empty()).when(employerRepository).findById(anyLong());

        DataResponse<Employer> dataResponse = employerService.get(1);

        Assertions.assertNull(dataResponse.getData());
        Assertions.assertEquals(ResponseMessage.EMPLOYER_IS_NOT_FOUND, dataResponse.getMessage());

        verify(employerRepository).findById(anyLong());
    }

    @DisplayName("Test getting all employer`s orders")
    @Test
    void test_get_all_employer_orders() {
        Employer employer = new Employer();
        employer.setId(1);

        Order order1 = new Order();
        Order order2 = new Order();

        employer.getOrderList().add(order1);
        employer.getOrderList().add(order2);

        doReturn(employer).when(employerRepository).getReferenceById(anyLong());

        DataResponse<List<Order>> dataResponse = employerService.getAllOrders(employer.getId());

        Assertions.assertEquals(employer.getOrderList(), dataResponse.getData());
        Assertions.assertNull(dataResponse.getMessage());

        verify(employerRepository).getReferenceById(employer.getId());
    }

    @DisplayName("Test getting all employer`s orders when employer is not found")
    @Test
    void test_get_all_employer_orders_when_employee_is_not_found() {
        doReturn(null).when(employerRepository).getReferenceById(anyLong());

        DataResponse<List<Order>> dataResponse = employerService.getAllOrders(1);

        Assertions.assertNull(dataResponse.getData());
        Assertions.assertEquals(ResponseMessage.EMPLOYER_IS_NOT_FOUND, dataResponse.getMessage());
    }
}
