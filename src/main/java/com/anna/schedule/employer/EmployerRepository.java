package com.anna.schedule.employer;

import com.anna.schedule.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployerRepository extends JpaRepository<Employer, String> {

    @Query(value = "select * from Employer where employer_id = ?1", nativeQuery = true)
    Employer getByPhoneNumber(String employerId);
}
