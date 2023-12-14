package com.anna.schedule.order;

import com.anna.schedule.employee.Employee;
import com.anna.schedule.employer.Employer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "Work_Order")
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="employee_id")
    private Employee employee;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="employer_id")
    private Employer employer;

    @NonNull
    @Column(name = "order_start_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startTime;

    @NonNull
    @Column(name = "order_end_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endTime;
}
