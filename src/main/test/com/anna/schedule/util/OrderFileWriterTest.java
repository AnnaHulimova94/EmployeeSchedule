package com.anna.schedule.util;

import com.anna.schedule.employee.Employee;
import com.anna.schedule.employer.Employer;
import com.anna.schedule.order.Order;
import com.anna.schedule.order.OrderRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public class OrderFileWriterTest {

    private List<Order> orderList;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("Test structure of order file")
    void test_order_file_structure() {
        OrderFileWriter orderFileWriter = new OrderFileWriter();
        Workbook workbook = orderFileWriter.generate(new ArrayList<>(), new Employee());
        Sheet sheet = workbook.getSheet("null null");
        Row row = sheet.getRow(0);

        Assertions.assertEquals("", row.getCell(0).getStringCellValue());
        Assertions.assertEquals("Coworker", row.getCell(1).getStringCellValue());
        Assertions.assertEquals("Coworker phone number", row.getCell(2).getStringCellValue());
        Assertions.assertEquals("Order start", row.getCell(3).getStringCellValue());
        Assertions.assertEquals("Order end", row.getCell(4).getStringCellValue());
    }

    @Test
    @DisplayName("Test file generation for employer")
    void test_order_file_generation_for_employer() {
        orderList = orderRepository.findAll().stream()
                .sorted(Comparator.comparing(Order::getStartTime))
                .collect(Collectors.toList());

        orderList.forEach(order -> test_order_file_generation_for_employer(order.getEmployer()));
    }

    @Test
    @DisplayName("Test file generation for employee")
    void test_order_file_generation_for_employee() {
        orderList = orderRepository.findAll().stream()
                .filter(order -> order.getEmployee() != null)
                .sorted(Comparator.comparing(Order::getStartTime))
                .collect(Collectors.toList());

        orderList.forEach(order -> test_order_file_generation_for_employee(order.getEmployee()));
    }

    private void test_order_file_generation_for_employer(Employer employer) {
        OrderFileWriter orderFileWriter = new OrderFileWriter();
        Workbook workbook = orderFileWriter.generate(orderList, employer);

        Sheet sheet = workbook.getSheet(employer.getLastName() + " " + employer.getFirstName());
        Row row;
        Order order;
        Employee employee;

        for (int i = 1; i <= orderList.size(); i++) {
            order = orderList.get(i - 1);
            employee = order.getEmployee();
            row = sheet.getRow(i);

            if (employee == null) {
                Assertions.assertNull(row.getCell(1));
                Assertions.assertNull(row.getCell(2));
            } else {
                Assertions.assertEquals(employee.getLastName() + " " + employee.getFirstName(),
                        row.getCell(1).getStringCellValue());
                Assertions.assertEquals(employee.getPhoneNumber(), row.getCell(2).getStringCellValue());
            }

            Assertions.assertEquals(i, row.getCell(0).getNumericCellValue());
            Assertions.assertEquals(order.getStartTime(), row.getCell(3).getLocalDateTimeCellValue());
            Assertions.assertEquals(order.getEndTime(), row.getCell(4).getLocalDateTimeCellValue());
        }
    }

    private void test_order_file_generation_for_employee(Employee employee) {
        orderList = orderList.stream()
                .filter(order -> employee.equals(order.getEmployee()))
                .collect(Collectors.toList());

        OrderFileWriter orderFileWriter = new OrderFileWriter();
        Workbook workbook = orderFileWriter.generate(orderList, employee);
        Sheet sheet = workbook.getSheet(employee.getLastName() + " " + employee.getFirstName());
        Row row;
        Order order;
        Employer employer;

        for (int i = 1; i <= orderList.size(); i++) {
            order = orderList.get(i - 1);
            employer = order.getEmployer();
            row = sheet.getRow(i);

            Assertions.assertEquals(i, row.getCell(0).getNumericCellValue());
            Assertions.assertEquals(employer.getLastName() + " " + employer.getFirstName(),
                    row.getCell(1).getStringCellValue());
            Assertions.assertEquals(employer.getPhoneNumber(), row.getCell(2).getStringCellValue());
            Assertions.assertEquals(order.getStartTime(), row.getCell(3).getLocalDateTimeCellValue());
            Assertions.assertEquals(order.getEndTime(), row.getCell(4).getLocalDateTimeCellValue());
        }
    }
}
