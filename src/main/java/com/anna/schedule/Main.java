package com.anna.schedule;

import com.anna.schedule.employee.Employee;
import com.anna.schedule.employee.EmployeeRepository;
import com.anna.schedule.employer.Employer;
import com.anna.schedule.employer.EmployerRepository;
import com.anna.schedule.order.Order;
import com.anna.schedule.order.OrderRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@SpringBootApplication
public class Main {

    private EmployerRepository employerRepository;

    private EmployeeRepository employeeRepository;

    private OrderRepository orderRepository;

    public Main(EmployerRepository employerRepository, EmployeeRepository employeeRepository, OrderRepository orderRepository) {
        this.employerRepository = employerRepository;
        this.employeeRepository = employeeRepository;
        this.orderRepository = orderRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @PostConstruct
    private void setUp() {
        Employee employee1 = new Employee();
        employee1.setFirstName("Hanna");
        employee1.setLastName("Hulimova");
        employee1.setPhoneNumber("+380504876134");

        employeeRepository.save(employee1);

        Employee employee2 = new Employee();
        employee2.setFirstName("Oleg");
        employee2.setLastName("Bily");
        employee2.setPhoneNumber("+872647637182");

        employeeRepository.save(employee2);

        Employer employer1 = new Employer();
        employer1.setFirstName("Helen");
        employer1.setLastName("Bila");
        employer1.setPhoneNumber("+342438765163");

        employerRepository.save(employer1);

        Employer employer2 = new Employer();
        employer2.setFirstName("Lera");
        employer2.setLastName("March");
        employer2.setPhoneNumber("+849385833123");

        employerRepository.save(employer2);

        int year = LocalDateTime.now().getYear();
        int month = LocalDateTime.now().getMonth().getValue();

        if (month == 12) {
            month = 1;
            year++;
        } else {
            month++;
        }

        Order order1 = new Order();
        order1.setEmployer(employer1);
        order1.setStartTime(LocalDateTime.of(year, month, 20, 12, 0));
        order1.setEndTime(LocalDateTime.of(year, month, 20, 15, 0));

        orderRepository.save(order1);

        Order order2 = new Order();
        order2.setEmployer(employer1);
        order2.setStartTime(LocalDateTime.of(year, month, 20, 11, 0));
        order2.setEndTime(LocalDateTime.of(year, month, 20, 16, 0));

        orderRepository.save(order2);

        Order order3 = new Order();
        order3.setEmployer(employer1);
        order3.setStartTime(LocalDateTime.of(year, month, 20, 14, 45));
        order3.setEndTime(LocalDateTime.of(year, month, 20, 17, 0));

        orderRepository.save(order3);

        Order order4 = new Order();
        order4.setEmployer(employer1);
        order4.setStartTime(LocalDateTime.of(year, month, 20, 13, 0));
        order4.setEndTime(LocalDateTime.of(year, month, 20, 14, 0));

        orderRepository.save(order4);

        Order order5 = new Order();
        order5.setEmployer(employer2);
        order5.setStartTime(LocalDateTime.of(year, month, 20, 11, 0));
        order5.setEndTime(LocalDateTime.of(year, month, 20, 14, 0));

        orderRepository.save(order5);

        Order order6 = new Order();
        order6.setEmployer(employer2);
        order6.setStartTime(LocalDateTime.of(year, month, 20, 10, 0));
        order6.setEndTime(LocalDateTime.of(year, month, 20, 12, 0));

        orderRepository.save(order6);
    }
}
