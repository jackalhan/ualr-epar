package com.jackalhan.ualr.repository;

import com.jackalhan.ualr.domain.model.WorkloadReport;
import com.jackalhan.ualr.domain.model.WorkloadReportTerm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by txcakaloglu on 5/16/16.
 */
public interface WorkloadReportRepository extends JpaRepository<WorkloadReport, Long> {

    WorkloadReport findByInstructorNameSurnameAndReportNameAndWorkloadReportTermId(String namesurname, String reportname, Long workloadreporttermid);

    List<WorkloadReport> findByWorkloadReportTermIdOrderByInstructorNameSurnameAsc(Long workloadreporttermid);

    @Query(value =
            "SELECT t.* " +
                    "FROM workload_report t join workload_report_term w " +
                    "WHERE t.workload_report_term_id=?1 GROUP BY t.department_name, t.department_code " +
                    "ORDER BY t.department_name asc", nativeQuery = true)
    List<WorkloadReport> listAllGroupByDepartmentNameAndCodeOrderedByDepartmentName(Long workloadreporttermid);

    @Query(value =
            "SELECT t.* " +
                    "FROM workload_report t join workload_report_term w " +
                    "WHERE t.workload_report_term_id=?1 and t.department_code=?2 GROUP BY t.department_name, t.department_code " +
                    "ORDER BY t.department_name asc", nativeQuery = true)
    List<WorkloadReport> listAllGroupByDepartmentNameAndCodeOrderedByDepartmentName(Long workloadreporttermid, String departmentCode);


    List<WorkloadReport> findByWorkloadReportTermIdAndDepartmentCodeOrderByInstructorNameSurnameAsc(Long workloadreporttermid, String departmentCode);

    List<WorkloadReport> findByWorkloadReportTermIdAndDepartmentCodeOrderByDepartmentCodeAsc(Long workloadreporttermid, String departmentCode);

    List<WorkloadReport> findByWorkloadReportTermIdOrderByDepartmentCode(Long workloadreporttermid);


}
