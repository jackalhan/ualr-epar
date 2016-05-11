package com.jackalhan.ualr.repository;

import com.jackalhan.ualr.domain.DepartmentStaff;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jackalhan on 5/10/16.
 */
public interface DepartmentStaffRepository extends JpaRepository<DepartmentStaff, Long> {
}
