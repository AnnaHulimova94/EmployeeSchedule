package com.anna.schedule.employee;

import com.anna.schedule.PersonInterface;
import com.anna.schedule.order.Order;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "Employee")
public class Employee implements PersonInterface {

    @Id
    @GeneratedValue
    @Column(name = "employee_id")
    private long id;

    @NotNull
    @Column(name = "employee_firstName")
    private String firstName;

    @NotNull
    @Column(name = "employee_lastName")
    private String lastName;

    @NonNull
    @Pattern(regexp = "\\+([0-9]{12})")
    @Column(name = "employee_phone_number", unique = true)
    private String phoneNumber;

    @OneToMany(mappedBy = "employee")
    private List<Order> orderList = new ArrayList<>();
}
