package com.anna.schedule.employer;

import com.anna.schedule.order.Order;
import com.anna.schedule.util.DataResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/employer")
public class EmployerController {

    private EmployerService employerService;

    public EmployerController(EmployerService employerService) {
        this.employerService = employerService;
    }

    @PostMapping
    public ResponseEntity<DataResponse<Employer>> add(@RequestBody Employer employer) {
        return new ResponseEntity<>(employerService.add(employer), HttpStatus.OK);
    }

    @GetMapping("/{employerId}")
    public ResponseEntity<DataResponse<Employer>> get(@PathVariable("employerId") long employerId) {
        return new ResponseEntity<>(employerService.get(employerId), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<DataResponse<List<Employer>>> getAll() {
        return new ResponseEntity<>(employerService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{employerId}/orders")
    public ResponseEntity<DataResponse<List<Order>>> getAllOrders(@PathVariable("employerId") long employerId) {
        return new ResponseEntity<>(employerService.getAllOrders(employerId), HttpStatus.OK);
    }

    @GetMapping("/{employerId}/schedule")
    public ResponseEntity<ByteArrayResource> getScheduleFile(@PathVariable("employerId") long employerId) throws IOException {
        try {
            Workbook workbook = employerService.getScheduleFile(employerId);

            if (workbook == null) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            HttpHeaders header = new HttpHeaders();

            header.setContentType(new MediaType("application", "force-download"));
            header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Schedule.xls");
            workbook.write(stream);
            workbook.close();

            return new ResponseEntity<>(new ByteArrayResource(stream.toByteArray()),
                    header, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
