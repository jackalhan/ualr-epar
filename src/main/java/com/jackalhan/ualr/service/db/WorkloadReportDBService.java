package com.jackalhan.ualr.service.db;

import com.jackalhan.ualr.domain.model.Faculty;
import com.jackalhan.ualr.domain.model.WorkloadReport;
import com.jackalhan.ualr.domain.model.WorkloadReportTerm;
import com.jackalhan.ualr.repository.WorkloadReportRepository;
import com.jackalhan.ualr.repository.WorkloadReportTermRepository;
import com.jackalhan.ualr.service.utils.StringUtilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by txcakaloglu on 5/16/16.
 */
@Service
@Transactional
public class WorkloadReportDBService {

    private final Logger log = LoggerFactory.getLogger(WorkloadReportDBService.class);

    @Autowired
    private WorkloadReportRepository workloadReportRepository;
    @Autowired
    private WorkloadReportTermRepository workloadReportTermRepository;


    @Transactional
    public WorkloadReportTerm createWorkloadReportTermIfNotFound(WorkloadReportTerm workloadReportTerm) {
        WorkloadReportTerm wlterm = workloadReportTermRepository.findBySemesterTermAndSemesterYearAndFacultyCodeAndImportedFileDate(workloadReportTerm.getSemesterTerm(), workloadReportTerm.getSemesterYear(), workloadReportTerm.getFaculty().getCode(), workloadReportTerm.getImportedFileDate());
        if (wlterm == null) {
            workloadReportTermRepository.save(workloadReportTerm);
            log.info("createWorkloadReportTermIfNotFound " + "saved data successfully");
        }
        return workloadReportTerm;
    }

    @Transactional
    public WorkloadReport createWorkloadReportIfNotFound(WorkloadReport workloadReport) {
        WorkloadReport wl = workloadReportRepository.findByInstructorNameSurnameAndReportNameAndWorkloadReportTermId(workloadReport.getInstructorNameSurname(), workloadReport.getReportName(), workloadReport.getWorkloadReportTerm().getId());
        if (wl == null) {
            workloadReportRepository.save(workloadReport);
            log.info("createWorkloadReportIfNotFound " + "saved data successfully");
        }
        return workloadReport;
    }

    @Transactional
    public void createWorkloadReportIfNotFoundAsBulk(List<WorkloadReport> workloadReport) {
        for (WorkloadReport wl1 : workloadReport) {
            WorkloadReport wl = workloadReportRepository.findByInstructorNameSurnameAndReportNameAndWorkloadReportTermId(wl1.getInstructorNameSurname(), wl1.getReportName(), wl1.getWorkloadReportTerm().getId());
            if (wl == null) {
                workloadReportRepository.save(workloadReport);
            }
        }

        log.info("createWorkloadReportIfNotFound " + "saved data successfully");

    }

    @Transactional
    public List<WorkloadReportTerm> listAllWorkloadReportTermsAndGroupByFacultyCodeAndYear() {
        log.info("listAllWorkloadReportTermsAndGroupByFacultyCodeAndYear " + " listed successfully");
        return workloadReportTermRepository.listAllGroupByFacultyCodeAndYear();
    }



    @Transactional
    public List<WorkloadReportTerm>  listAllTermsBasedOnFacultyAndYear(String facultyCode, int semesterYear) {
        log.info("listAllWorkloadReportTermsAndGroupByFacultyCodeAndYear " + " listed successfully");
        return workloadReportTermRepository.findByFacultyCodeAndSemesterYearOrderBySemesterTermCodeAscImportedFileDateDesc(facultyCode, semesterYear);
    }

    @Transactional
    public WorkloadReportTerm  listOneWorkloadReportTermBasedOnId(Long workloadReportTermId) {
        log.info("listOneWorkloadReportTermBasedOnId " + " listed successfully");
        return workloadReportTermRepository.findOne(workloadReportTermId);
    }

    @Transactional
    public List<WorkloadReport>  listAllWorkloadReportsBasedOnTermIdAndDepartmentCode(Long workloadReportTermId, String departmentCode) {
        log.info("listAllWorkloadReportsBasedOnTermId " + " listed successfully");
        return workloadReportRepository.findByWorkloadReportTermIdAndDepartmentCodeOrderByInstructorNameSurnameAsc(workloadReportTermId, departmentCode);
    }

    @Transactional
    public List<WorkloadReport>  listAllGroupByDepartmentNameAndCodeOrderedByDepartmentNameBasedOnTermId(Long workloadReportTermId) {
        log.info("listAllWorkloadReportsBasedOnTermId " + " listed successfully");
        return workloadReportRepository.listAllGroupByDepartmentNameAndCodeOrderedByDepartmentName(workloadReportTermId);
    }

    @Transactional
    public WorkloadReport listOneWorkloadReportsBasedOnId(Long workloadReportId) {
        log.info("listOneWorkloadReportsBasedOnId " + " listed successfully");
        return workloadReportRepository.getOne(workloadReportId);
    }

    @Transactional
    public List<WorkloadReport>  listAllWorkloadReportsBasedOnWorkloadReportTermIdAndDepartmentCode(Long workloadReportTermId, String departmentCode) {
        log.info("listAllWorkloadReportsBasedOnWorkloadReportTermIdAndDepartmentCode " + " listed successfully");
        return workloadReportRepository.findByWorkloadReportTermIdAndDepartmentCodeOrderByInstructorNameSurnameAsc(workloadReportTermId, departmentCode);
    }

    @Transactional
    public List<WorkloadReport>  listAllWorkloadReportsBasedOnWorkloadReportTermId(Long workloadReportTermId) {
        log.info("listAllWorkloadReportsBasedOnWorkloadReportTermId " + " listed successfully");
        return workloadReportRepository.findByWorkloadReportTermIdOrderByInstructorNameSurnameAsc(workloadReportTermId);
    }



}
