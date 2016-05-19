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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

/**
 * Created by jackalhan on 5/15/16.
 */
@Controller
public class WorkloadController {

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

    private String calculateDynamicDivSizeOfFacultyName(int facultyNameLength){
        double block =  facultyNameLength/ 9;
        int partSize = (int) Math.ceil(block);
        return "col-md-" + partSize;
    }
}
