package com.jackalhan.ualr.controller;

import com.jackalhan.ualr.constant.GenericConstant;
import com.jackalhan.ualr.domain.model.WorkloadReport;
import com.jackalhan.ualr.domain.model.WorkloadReportDetails;
import com.jackalhan.ualr.service.rest.LoginService;
import com.jackalhan.ualr.service.utils.FileUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

/**
 * Created by jackalhan on 5/15/16.
 */
@Controller
public class WorkloadController {


    @RequestMapping("/workload")
    public String workload (@RequestParam(value="name", required=false, defaultValue="World") String name, Model model, Principal principal) {
        LoginService loginService = new LoginService();
        model.addAttribute("username", loginService.getUserName(principal));
        model.addAttribute("userroles", loginService.getUserRoles(principal));
        List<WorkloadReport> files=FileUtilService.getInstance().listFolders(GenericConstant.WORKLOAD_REPORTS_EXCEL_PROCESSED_PATH);

        model.addAttribute("workloadReports", files);

        return "workload";
    }
}
