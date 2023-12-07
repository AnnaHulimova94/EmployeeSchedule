package com.anna.schedule.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query(value = "select * from Work_Order o where o.employee_id is null", nativeQuery = true)
    List<Order> getFreeOrderList();
}
