package com.anna.schedule.employee;

import com.sun.istack.internal.NotNull;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "Employee")
public class Employee {
    @Id
    @Column(name = "employee_phone_number")
    public String phoneNumber;

    @NotNull
    @Column(name = "employee_first_name")
    private String firstName;

    @NotNull
    @Column(name = "employee_last_name")
    private String lastName;

    @NotNull
    @Column(name = "employee_category")
    private Category category;
}
