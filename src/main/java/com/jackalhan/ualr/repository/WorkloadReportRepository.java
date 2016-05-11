package com.jackalhan.ualr.repository;

import com.jackalhan.ualr.domain.WorkloadReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by jackalhan on 5/10/16.
 */

public interface WorkloadReportRepository extends JpaRepository<WorkloadReport, Long> {
}
