package com.anna.schedule.employer;

import com.anna.schedule.order.Order;
import com.anna.schedule.util.DataResponse;
import com.anna.schedule.util.ResponseMessage;
import com.anna.schedule.util.OrderFileWriter;
import org.apache.poi.ss.usermodel.Workbook;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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

    public DataResponse<Employer> getByPhoneNumber(String phoneNumber) {
        Employer employer = employerRepository.getByPhoneNumber(phoneNumber);

        return employer == null
                ? new DataResponse<>(null, ResponseMessage.EMPLOYER_IS_NOT_FOUND)
                : new DataResponse<>(employer, null);
    }

    public DataResponse<Employer> get(long id) {
        Employer employer = employerRepository.findById(id).orElse(null);

        return employer == null
                ? new DataResponse<>(null, ResponseMessage.EMPLOYER_IS_NOT_FOUND)
                : new DataResponse<>(employer, null);
    }

    public DataResponse<List<Order>> getAllOrders(long id) {
        Employer employer = employerRepository.findById(id).orElse(null);

        return employer == null
                ? new DataResponse<>(null, ResponseMessage.EMPLOYER_IS_NOT_FOUND)
                : new DataResponse<>(employer.getOrderList(), null);
    }

    public Workbook getScheduleFile(long id) {
        Employer employer = employerRepository.findById(id).orElse(null);

        if (employer == null) {
            return null;
        }

        return new OrderFileWriter().generate(employer.getOrderList(), employer);
    }
}
