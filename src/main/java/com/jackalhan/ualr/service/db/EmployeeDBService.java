package com.jackalhan.ualr.service.db;

import com.jackalhan.ualr.domain.model.Employee;
import com.jackalhan.ualr.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by txcakaloglu on 6/23/16.
 */
@Service
@Transactional
public class EmployeeDBService {

        private final Logger log = LoggerFactory.getLogger(EmployeeDBService.class);

        @Autowired
        private EmployeeRepository employeeRepository;


        @Transactional
        public Employee createFacultyIfNotFound(Employee employee) {
            Employee emp = employeeRepository.findByNetid(employee.getNetid());
            if (emp == null) {
                employeeRepository.save(employee);
                log.info("createFacultyIfNotFound " + "saved data successfully");
            }
            return emp;
        }
}
