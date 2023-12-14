package com.anna.schedule.employer;

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
    @Column(name = "employer_phone_number", unique = true)
    private String phoneNumber;

    @OneToMany(mappedBy = "employer")
    private List<Order> orderList = new ArrayList<>();

    @Override
    public String toString() {
        return "" + id;
    }
}
