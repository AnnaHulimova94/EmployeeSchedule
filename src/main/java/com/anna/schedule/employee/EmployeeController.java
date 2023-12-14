package com.anna.schedule.employee;

import com.anna.schedule.order.Order;
import com.anna.schedule.util.DataResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<DataResponse<Employee>> add(@RequestBody Employee employee) {
        return new ResponseEntity<>(employeeService.add(employee), HttpStatus.OK);
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<DataResponse<Employee>> get(@PathVariable("employeeId") long employeeId) {
        return new ResponseEntity<>(employeeService.get(employeeId), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<DataResponse<List<Employee>>> getAll() {
        return new ResponseEntity<>(employeeService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{employeeId}/orders")
    public ResponseEntity<DataResponse<List<Order>>> getAllOrders(@PathVariable("employeeId") long employeeId) {
        return new ResponseEntity<>(employeeService.getAllOrders(employeeId), HttpStatus.OK);
    }

    @GetMapping("/{employeeId}/schedule")
    public ResponseEntity<ByteArrayResource> getScheduleFile(HttpServletResponse response,
                                                             @PathVariable("employeeId") long employeeId) throws IOException {
        try {
            Workbook workbook = employeeService.getScheduleFile(employeeId);

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
