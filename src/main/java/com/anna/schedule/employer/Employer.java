package com.anna.schedule.employer;

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
@Table(name = "Employer")
public class Employer {

    @Id
    @Column(name = "employer_id")
    @Pattern(regexp = "\\+([0-9]{12})")
    private String id;

    @NotNull
    @Column(name = "employer_firstName")
    private String firstName;

    @NotNull
    @Column(name = "employer_lastName")
    private String lastName;

    @OneToMany(mappedBy="employer")
    private List<Order> orderList = new ArrayList<>();
}
