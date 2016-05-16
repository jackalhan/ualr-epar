package com.jackalhan.ualr.repository;

import com.jackalhan.ualr.domain.model.WorkloadReport;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by txcakaloglu on 5/16/16.
 */
public interface WorkloadReportRepository extends JpaRepository<WorkloadReport, Long> {

    WorkloadReport findByInstructorNameSurnameAndReportNameAndWorkloadReportTermId(String namesurname, String reportname, Long workloadreporttermid);
}
