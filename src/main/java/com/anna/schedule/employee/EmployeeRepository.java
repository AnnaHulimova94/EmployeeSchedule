package com.anna.schedule.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {

    @Query(value = "select * from Employee where employee_id = ?1", nativeQuery = true)
    Employee getByPhoneNumber(String employeeId);
}
