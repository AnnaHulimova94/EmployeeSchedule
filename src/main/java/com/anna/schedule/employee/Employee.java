package com.anna.schedule.employee;

import com.anna.schedule.order.Order;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
@Entity
@Table(name = "Employee")
public class Employee {

    @Id
    @Column(name = "employee_id")
    @Pattern(regexp = "\\+([0-9]{12})")
    private String id;

    @NotNull
    @Column(name = "employee_firstName")
    private String firstName;

    @NotNull
    @Column(name = "employee_lastName")
    private String lastName;

    @OneToMany(mappedBy="employee")
    private List<Order> orderList = new ArrayList<>();
}
