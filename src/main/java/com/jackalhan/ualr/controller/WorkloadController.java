package com.jackalhan.ualr.controller;

import com.jackalhan.ualr.constant.GenericConstant;
import com.jackalhan.ualr.domain.model.Faculty;
import com.jackalhan.ualr.domain.model.WorkloadReport;
import com.jackalhan.ualr.domain.model.WorkloadReportTerm;
import com.jackalhan.ualr.repository.WorkloadReportTermRepository;
import com.jackalhan.ualr.service.db.FacultyDBService;
import com.jackalhan.ualr.service.db.WorkloadReportDBService;
import com.jackalhan.ualr.service.rest.LoginService;
import com.jackalhan.ualr.service.utils.FileUtilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

/**
 * Created by jackalhan on 5/15/16.
 */
@Controller
public class WorkloadController {

    private final Logger log = LoggerFactory.getLogger(WorkloadController.class);

    @Autowired
    private WorkloadReportDBService workloadReportDBService;

    @Autowired
    private FacultyDBService facultyDBService;

    @RequestMapping("workload_reports_terms")
    public String workload(@RequestParam(value = "name", required = false, defaultValue = "World") String name, Model model, Principal principal) {
        LoginService loginService = new LoginService();
        model.addAttribute("username", loginService.getUserName(principal));
        model.addAttribute("userroles", loginService.getUserRoles(principal));
        List<WorkloadReportTerm> records = workloadReportDBService.listAllWorkloadReportTermsAndGroupByFacultyCodeAndYear();
        model.addAttribute("workloadsReportTerms", records);

        return "workload_reports_terms";
    }

    @RequestMapping("workload_reports_faculty_terms")
    public String listWorkloadReportsBasedOnFacultyAndTerms(String facultyCode, int semesterTermYear, Model model, Principal principal) {
        LoginService loginService = new LoginService();
        Faculty faculty = facultyDBService.findByCode(facultyCode);
        List<WorkloadReportTerm> records = workloadReportDBService.listAllTermsBasedOnFacultyAndYear(facultyCode, semesterTermYear);


        model.addAttribute("facultyWorkloadsReportTerms", records);
        model.addAttribute("username", loginService.getUserName(principal));
        model.addAttribute("userroles", loginService.getUserRoles(principal));
        model.addAttribute("facultyName", faculty.getName());
        model.addAttribute("semesterYear", semesterTermYear);
        model.addAttribute("dynamicColumnClassName", calculateDynamicDivSizeOfFacultyName(faculty.getName().length()));

        return "workload_reports_faculty_terms";
    }

    @RequestMapping("workload_reports")
    public String listWorkloadReports(Long workloadReportTermId, Model model, Principal principal) {
        LoginService loginService = new LoginService();
        WorkloadReportTerm workloadReportTerm = workloadReportDBService.listOneWorkloadReportTermBasedOnId(workloadReportTermId);
        Faculty faculty = facultyDBService.findByCode(workloadReportTerm.getFaculty().getCode());
        List<WorkloadReport> workloadReports = workloadReportDBService.listAllWorkloadReportsBasedOnTermId(workloadReportTermId);

        model.addAttribute("workloadReports", workloadReports);
        model.addAttribute("username", loginService.getUserName(principal));
        model.addAttribute("userroles", loginService.getUserRoles(principal));
        model.addAttribute("facultyName", faculty.getName());
        model.addAttribute("semesterYear", workloadReportTerm.getSemesterYear());
        model.addAttribute("semesterTermName", workloadReportTerm.getSemesterTerm());
        model.addAttribute("dynamicColumnClassName", calculateDynamicDivSizeOfFacultyName(faculty.getName().length()));

        return "workload_reports";
    }

    @RequestMapping(value = "workload_reports_downloaded", method = RequestMethod.GET)
    public String getWorkloadReport(@RequestParam("workloadReportId") Long workloadReportId, @RequestParam("workloadReportTermId") Long workloadReportTermId, HttpServletResponse httpServletResponse, Model model, Principal principal) {
        String reportName = null;
        try {
            LoginService loginService = new LoginService();
            WorkloadReportTerm workloadReportTerm = workloadReportDBService.listOneWorkloadReportTermBasedOnId(workloadReportTermId);
            Faculty faculty = facultyDBService.findByCode(workloadReportTerm.getFaculty().getCode());

            WorkloadReport workloadReport = workloadReportDBService.listOneWorkloadReportsBasedOnId(workloadReportId);
            reportName = workloadReport.getReportName();
            model.addAttribute("username", loginService.getUserName(principal));
            model.addAttribute("userroles", loginService.getUserRoles(principal));
            model.addAttribute("facultyName", faculty.getName());
            model.addAttribute("semesterYear", workloadReportTerm.getSemesterYear());
            model.addAttribute("semesterTermName", workloadReportTerm.getSemesterTerm());
            model.addAttribute("dynamicColumnClassName", calculateDynamicDivSizeOfFacultyName(faculty.getName().length()));
            model.addAttribute("instructorNameSurname", workloadReport.getInstructorNameSurname());
            model.addAttribute("departmentName", workloadReport.getDepartmentName());
            model.addAttribute("reportName", workloadReport.getReportName());

            httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + reportName);
            httpServletResponse.setContentType("application/vnd.ms-excel");
            httpServletResponse.getOutputStream().write(workloadReport.getReport());
            httpServletResponse.flushBuffer();

        } catch (IOException ex) {
            log.error("Error writing file to output stream. Filename was '{}'", reportName, ex);
        }
        return "workload_reports_downloaded";

    }

    private String calculateDynamicDivSizeOfFacultyName(int facultyNameLength) {
        double block = facultyNameLength / 9;
        int partSize = (int) Math.ceil(block);
        return "col-md-" + partSize;
    }
}
