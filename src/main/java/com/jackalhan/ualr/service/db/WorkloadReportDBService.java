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
        WorkloadReportTerm wlterm = workloadReportTermRepository.findBySemesterTermAndSemesterYearAndFacultyCode(workloadReportTerm.getSemesterTerm(), workloadReportTerm.getSemesterYear(), workloadReportTerm.getFaculty().getCode());
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


}
