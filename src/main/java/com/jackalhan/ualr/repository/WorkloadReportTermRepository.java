package com.jackalhan.ualr.repository;

import com.jackalhan.ualr.domain.model.WorkloadReportTerm;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by txcakaloglu on 5/16/16.
 */
public interface WorkloadReportTermRepository extends JpaRepository<WorkloadReportTerm, Long> {

    WorkloadReportTerm findBySemesterTermAndSemesterYearAndFacultyCode(String term, int year, String facultyCode);

}
