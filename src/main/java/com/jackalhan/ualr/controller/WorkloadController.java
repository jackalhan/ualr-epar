package com.jackalhan.ualr.controller;

import com.jackalhan.ualr.constant.GenericConstant;
import com.jackalhan.ualr.domain.model.Faculty;
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

    @RequestMapping("/workload/all")
    public String workload(@RequestParam(value = "name", required = false, defaultValue = "World") String name, Model model, Principal principal) {
        LoginService loginService = new LoginService();
        model.addAttribute("username", loginService.getUserName(principal));
        model.addAttribute("userroles", loginService.getUserRoles(principal));
        List<WorkloadReportTerm> records = workloadReportDBService.listAllWorkloadReportTermsAndGroupByFacultyCodeAndYear();
        model.addAttribute("workloadsReportTerms", records);

        return "workload_reports_terms";
    }

    @RequestMapping("/workload/faculty/{facultyCode}/{semesterTermYear}")
    public String listWorkloadReportsBasedOnFacultyAndTerms(@PathVariable String facultyCode, @PathVariable int semesterTermYear, Model model, Principal principal) {
        LoginService loginService = new LoginService();
        model.addAttribute("username", loginService.getUserName(principal));
        model.addAttribute("userroles", loginService.getUserRoles(principal));
        Faculty faculty = facultyDBService.
        List<WorkloadReportTerm> records = workloadReportDBService.listAllWorkloadReportTermsAndGroupByFacultyCodeAndYear();

        return "workload_reports_faculty_terms";
    }
}
