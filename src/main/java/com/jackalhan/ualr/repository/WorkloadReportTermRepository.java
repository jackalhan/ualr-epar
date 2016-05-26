package com.jackalhan.ualr.repository;

import com.jackalhan.ualr.domain.model.WorkloadReportTerm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by txcakaloglu on 5/16/16.
 */
public interface WorkloadReportTermRepository extends JpaRepository<WorkloadReportTerm, Long> {

    WorkloadReportTerm findBySemesterTermAndSemesterYearAndFacultyCodeAndImportedFileDate(String term, int year, String facultyCode, String importedFileDate);

    @Query(value =
            "SELECT t.* " +
            "FROM workload_report_term t join faculty f " +
            "group by t.faculty_code, t.semester_year", nativeQuery = true)
    List<WorkloadReportTerm> listAllGroupByFacultyCodeAndYear();

    List<WorkloadReportTerm> findByFacultyCodeAndSemesterYearOrderBySemesterTermCodeAscImportedFileDateDesc (String facultyCode, int semesterYear);

}
