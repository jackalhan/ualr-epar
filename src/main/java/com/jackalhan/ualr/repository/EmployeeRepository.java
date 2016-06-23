package com.jackalhan.ualr.repository;

import com.jackalhan.ualr.domain.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by txcakaloglu on 6/23/16.
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Employee findByNetid(String netid);

}
