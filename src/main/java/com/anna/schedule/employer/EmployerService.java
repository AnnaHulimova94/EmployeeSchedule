package com.anna.schedule.employer;

import com.anna.schedule.order.Order;
import com.anna.schedule.util.DataResponse;
import com.anna.schedule.util.ResponseMessage;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmployerService {

    private EmployerRepository employerRepository;

    public EmployerService(EmployerRepository employerRepository) {
        this.employerRepository = employerRepository;
    }

    public DataResponse<Employer> add(Employer employer) {
        if (employerRepository.findById(employer.getId()).isPresent()) {
            return new DataResponse<>(employer, ResponseMessage.EMPLOYER_ALREADY_EXITS);
        }

        return new DataResponse<>(employerRepository.save(employer), null);
    }

    public DataResponse<Employer> get(String id) {
        Employer employer = employerRepository.getByPhoneNumber(id);

        return employer == null
                ? new DataResponse<>(null, ResponseMessage.EMPLOYER_IS_NOT_FOUND)
                : new DataResponse<>(employer, null);
    }

    public DataResponse<List<Order>> getAllOrders(String id) {
        Employer employer = employerRepository.getByPhoneNumber(id);

        return employer == null
                ? new DataResponse<>(null, ResponseMessage.EMPLOYER_IS_NOT_FOUND)
                : new DataResponse<>(employer.getOrderList(), null);
    }
}
