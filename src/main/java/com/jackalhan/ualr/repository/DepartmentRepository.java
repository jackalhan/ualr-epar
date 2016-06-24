package com.jackalhan.ualr.repository;

import com.jackalhan.ualr.domain.model.Department;
import com.jackalhan.ualr.domain.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by txcakaloglu on 6/23/16.
 */
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Department findByCodeAndFacultyCode(String code, Faculty faculty);
}
