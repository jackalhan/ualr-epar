package com.jackalhan.ualr.repository;

import com.jackalhan.ualr.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jackalhan on 5/10/16.
 */
public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
