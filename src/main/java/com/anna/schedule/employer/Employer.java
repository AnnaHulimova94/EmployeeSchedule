package com.anna.schedule.employer;

import com.anna.schedule.PersonInterface;
import com.anna.schedule.order.Order;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
@Entity
@Table(name = "Employer")
public class Employer implements PersonInterface {

    @Id
    @GeneratedValue
    @Column(name = "employer_id")
    private long id;

    @NotNull
    @Column(name = "employer_firstName")
    private String firstName;

    @NotNull
    @Column(name = "employer_lastName")
    private String lastName;

    @NonNull
    @Pattern(regexp = "\\+([0-9]{12})")
    @Column(name = "employer_phone_number")
    private String phoneNumber;

    @OneToMany(mappedBy = "employer")
    private List<Order> orderList = new ArrayList<>();
}
