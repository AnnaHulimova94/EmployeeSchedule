package com.anna.schedule.employer;

import com.anna.schedule.order.Order;
import com.anna.schedule.util.DataResponse;
import com.anna.schedule.util.ResponseMessage;
import com.anna.schedule.util.OrderFileWriter;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmployerService {

    private EmployerRepository employerRepository;

    public EmployerService(EmployerRepository employerRepository) {
        this.employerRepository = employerRepository;
    }

    public DataResponse<Employer> add(Employer employer) {
        if (employerRepository.getByPhoneNumber(employer.getPhoneNumber()) != null) {
            return new DataResponse<>(null, ResponseMessage.PHONE_NUMBER_ALREADY_IN_USE);
        }

        if (employer.getPhoneNumber() == null
                || !employer.getPhoneNumber().matches("\\+([0-9]{12})")) {
            return new DataResponse<>(null, ResponseMessage.PHONE_NUMBER_FORMAT_IS_INVALID);
        }

        return new DataResponse<>(employerRepository.save(employer), null);
    }

    public DataResponse<Employer> get(long id) {
        Employer employer = employerRepository.findById(id).orElse(null);

        return employer == null
                ? new DataResponse<>(null, ResponseMessage.EMPLOYER_IS_NOT_FOUND)
                : new DataResponse<>(employer, null);
    }

    public DataResponse<List<Employer>> getAll() {
        return new DataResponse<>(employerRepository.findAll(), null);
    }

    public DataResponse<List<Order>> getAllOrders(long id) {
        Employer employer = employerRepository.getReferenceById(id);

        return employer == null
                ? new DataResponse<>(null, ResponseMessage.EMPLOYER_IS_NOT_FOUND)
                : new DataResponse<>(employer.getOrderList(), null);
    }

    public Workbook getScheduleFile(long id) {
        Employer employer = employerRepository.getReferenceById(id);

        if (employer == null) {
            return null;
        }

        return new OrderFileWriter().generate(employer.getOrderList(), employer);
    }
}
